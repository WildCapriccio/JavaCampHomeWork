package com.lagou.edu.mvcframework.servlet;

import com.lagou.edu.mvcframework.annotations.*;
import com.lagou.edu.mvcframework.exceptions.YuException;
import com.lagou.edu.mvcframework.pojo.Handler;
import com.lagou.demo.interceptors.LoginInterceptor;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YuDispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();

    private List<String> classNames = new LinkedList<>();  // Store all single java classes' names

    private Map<String, Object> ioc = new HashMap<>();  // IoC container

//  private Map<String, Method> handlerMapping = new HashMap<>();  // url:method
// 由于Handler class中包含了所有所需信息，故不再需要hashmap
    private List<Handler> handlerMapping = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // Step 1: load configuration files into memory: springmvc.properties
        // 需在web.xml中<servlet><init-param>中传入该配置文件
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        doLoadConfig(contextConfigLocation);

        // Step 2: scan classes and annotations
        doScan(properties.getProperty("scanPackage"));

        // Step 3: beans initialization (实现IoC容器, 基于注解)
        doInstance();


        // Step 4: 实现依赖注入
        doAutowired();

        // Step 5: 构造一个HandlerMapping，将配置好的url和Method建立映射关系
        initHandlerMapping();

        System.out.println("yumvc 初始化完成....");

        // Step 6: 等待请求进入，处理请求
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) return;

        // 扫描Controller class 和 class中的方法来获得注解中的url
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> aClass = entry.getValue().getClass();
            if (!aClass.isAnnotationPresent(YuController.class)) {
                continue;
            }

            String baseUrl = "";
            if (aClass.isAnnotationPresent(YuRequestMapping.class)) {
                YuRequestMapping annotation = aClass.getAnnotation(YuRequestMapping.class);
                baseUrl = annotation.value();   // 获得 "/demo"
            }

            LoginInterceptor loginInterceptor = null;
            // 遍历Object中的有RequestMapping注解的方法找剩下的url
            Method[] methods = aClass.getMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (!method.isAnnotationPresent(YuRequestMapping.class)) {
                    continue;
                }

                /*
                * 【作业1】
                * If current method has YuSecurity annotation,
                * then it should be a LoginInterceptor.
                * And it should be put into Handler object.
                * */
                if (method.isAnnotationPresent(YuSecurity.class)) {
                    YuSecurity anno = method.getAnnotation(YuSecurity.class);
                    String[] allowedUsers = anno.value();
                    loginInterceptor = new LoginInterceptor();
                    Set<String> whiteList = new HashSet<>();
                    for (String allowedUser : allowedUsers) {
                        whiteList.add(allowedUser);
                    }
                    loginInterceptor.setAllowedUsers(whiteList);
                }

                YuRequestMapping annotation = method.getAnnotation(YuRequestMapping.class);
                String methodUrl = annotation.value();  // 获得"/query"
                String url = baseUrl + methodUrl;   // "/demo/query"

                // 封装bean对象，method，url进Handler
                Handler handler = new Handler(entry.getValue(), method, Pattern.compile(url));
                // Add the LoginInterceptor into handler
                if (loginInterceptor != null) {
                    handler.getInterceptors().add(loginInterceptor);
                }

                /*
                * 最后，计算当前method的参数位置信息
                * 例如 query(HttpServletRequest request, HttpServletResponse response, String name)
                * */
                Parameter[] parameters = method.getParameters();
                for (int j = 0; j < parameters.length; j++) {
                    Parameter parameter = parameters[j];

                    // 这里只判断参数类型为 HttpServletRequest 或 HttpServletResponse 或 其他普通类型
                    if (parameter.getType() == HttpServletRequest.class
                    || parameter.getType() == HttpServletResponse.class) {
                        // simpleName 即 HttpServletResponse 或者 HttpServletRequest
                        handler.getParamIndexMapping().put(parameter.getType().getSimpleName(), j);
                    } else {
                        handler.getParamIndexMapping().put(parameter.getName(), j);  // 即put了 <name, 2>
                    }
                }

                handlerMapping.add(handler);
            }
        }
    }

    private void doAutowired() {
        if (ioc.isEmpty()) return;

        // 遍历ioc中所有对象，查看对象中的属性是否有autowire注解，若有才维护依赖注入关系
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            // 获取当前bean object的所有属性
            Field[] attributes = entry.getValue().getClass().getDeclaredFields();

            for (int i = 0; i < attributes.length; i++) {
                Field attribute = attributes[i];
                if (!attribute.isAnnotationPresent(YuAutowired.class)) {
                    continue;
                }

                // 找到了有Autowire注解的attribute
                YuAutowired annotation = attribute.getAnnotation(YuAutowired.class);
                String id = annotation.value();
                if (id.trim().isEmpty()) {   // 若没有指定value，则根据attribute的Interface注入
                    id = attribute.getType().getName();  // IDemoService
                }
                attribute.setAccessible(true);
                try {
                    /*
                     *  field.set(obj, value)
                     *  obj = the object whose field should be modified
                     *  value = the new value for the field of obj
                     * */
                    //                  parentObj ,  childObj
                    attribute.set(entry.getValue(), ioc.get(id));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 基于classNames中 类的全限定类名，以及反射技术，完成对象创建和管理
    private void doInstance() {
        if (classNames.size() == 0) return;

        try {
            for (int i = 0; i < classNames.size(); i++) {
                String className = classNames.get(i);  // com.lagou.demo.controller.DemoController

                // Reflection
                Class<?> aClass = Class.forName(className);

                // 区分 Controller 和 Service 来对id进行处理
                if (aClass.isAnnotationPresent(YuController.class)) {
                    Object o = aClass.newInstance();

                    // controller的id此处不做过多处理，不取value了，就拿类的首字母小写作为id，保存到ioc中
                    String simpleName = aClass.getSimpleName();  // DemoController
                    String id = lowerFirstLetter(simpleName);  // id = demoController

                    ioc.put(id, o);
                } else if (aClass.isAnnotationPresent(YuService.class)) {
                    Object o = aClass.newInstance();

                    YuService annotation = aClass.getAnnotation(YuService.class);
                    String beanName = annotation.value();

                    if (!beanName.trim().isEmpty()) {  // has value, then id = value
                        ioc.put(beanName, o);
                    }else {
                        beanName = lowerFirstLetter(aClass.getSimpleName());
                        ioc.put(beanName, o);
                    }

                    /*
                    * Service 层往往是有Interface的（面向Interface开发），此时再以Interface的全类名为id，存入ioc中，
                    * 便于之后根据Interface 类型注入
                    * */
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (int j = 0; j < interfaces.length; j++) {
                        Class<?> singleInterface = interfaces[j];
                        ioc.put(singleInterface.getName(), o);
                    }
                } else {
                    continue;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String lowerFirstLetter(String str) {
        char[] array = str.toCharArray();
        if ('A' <= array[0] && array[0] <= 'Z') {
            array[0] += 32;
        }
        return String.valueOf(array);
    }

    private void doScan(String scanPackage) {
        // scanPackage = com.lagou.demo --> 包的磁盘路径，即转换成文件夹模式 com/lagou/demo
        String diskPath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                + scanPackage.replaceAll("\\.", "/");
        File folder = new File(diskPath);

        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {  // sub package
                doScan(scanPackage + "." + file.getName());  // com.lagou.demo.controller
            }else if (file.getName().endsWith(".class")) {  // a single java class
                String className = scanPackage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);

        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    // 根据url，找到对应的Handler并调用其中的Method
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Step 1: 从HandlerMapping中查是否有能匹配上当前 request 里面 uri 的Handler
        Handler handler = getHandler(req);
        if (handler == null) {
            resp.getWriter().write("404 not found");
            return;
        }

        /*
        * Step 2: 参数绑定：
        *   1. 看method需要哪些参数
        *   2. 创建新array, named paramValues，准备向其中塞method所需要的参数值
        *   3. 遍历request中的所有参数，这里先只处理普通参数
        *   4. 再直接处理HttpServletRequest 和 HttpServletResponse
        * */

        Class<?>[] methodParams = handler.getMethod().getParameterTypes();
        int len = methodParams.length;
        Object[] paramValues = new Object[len];

        Map<String, String[]> requestParamMap = req.getParameterMap();  // 同一个参数名可能是个array，
        for (Map.Entry<String, String[]> entry : requestParamMap.entrySet()) {
            // name=Lisa&name=Jenny --> <name, [Lisa,Jenny]>
            String value = StringUtils.join(entry.getValue(), ",");  // value = "Lisa,Jenny"

            // 如果参数和方法中的参数匹配上了，就继续填充参数值进新array
            if (!handler.getParamIndexMapping().containsKey(entry.getKey())) {
                continue;
            }
            String paramName = entry.getKey();  // paramName = "name"
            Integer index = handler.getParamIndexMapping().get(paramName);  // "name" 对应于index为2的位置
            paramValues[index] = value;
        }

        // 处理 HttpServletRequest 和 HttpServletResponse
        int requestIndex = handler.getParamIndexMapping().get(HttpServletRequest.class.getSimpleName());  // = 0
        if (0 <= requestIndex && requestIndex < len) {
            paramValues[requestIndex] = req;
        }
        int responseIndex = handler.getParamIndexMapping().get(HttpServletResponse.class.getSimpleName()); // = 1
        if (0 <= responseIndex && responseIndex < len) {
            paramValues[responseIndex] = resp;
        }

        // Step 3: 最终调用Handler中的Method （传入手动塞好的paramValues 作为method的参数）
        try {
            // At first, check if handler has any interceptors.
            List<Object> interceptors = handler.getInterceptors();
            if (!interceptors.isEmpty()) {
                for (Object interceptor : interceptors) {
                    Method[] methods = interceptor.getClass().getMethods();
                    for (int i = 0; i < methods.length; i++) {
                        Method method = methods[i];
                        if (method.getName().equalsIgnoreCase("preHandle")) {
                            boolean preHandleResult = (boolean) method.invoke(interceptor, req);
                            if (!preHandleResult) {
                                throw new YuException("Current user (" + req.getParameter("username") + ") is NOT allowed to access.");
                            }
                        }
                    }
                }
            }

            handler.getMethod().invoke(handler.getController(), paramValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (YuException e) {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.print(e.getMessage());
            out.close();
        }
    }

    private Handler getHandler(HttpServletRequest req) {
        if (handlerMapping.isEmpty()) {
            return null;
        }

        String url = req.getRequestURI();  // 拿uri，而不是url

        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.getPattern().matcher(url);
            if (!matcher.matches()) {   // 如果url不匹配Handler的pattern
                continue;
            }
            return handler;
        }
        return null;
    }
}
