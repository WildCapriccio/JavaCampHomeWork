package com.lagou.edu.factory;

import com.lagou.edu.annotation.MyAutowired;
import com.lagou.edu.annotation.MyComponent;
import com.lagou.edu.annotation.MyService;
import com.lagou.edu.annotation.MyTransactional;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 应癫
 *
 * 工厂类，生产对象（使用反射技术）
 */
public class BeanFactory {

    /**
     * 任务一：通过Scan各种Annotation，把 id:Object 存入map，把 className:id 存入 class2IdMap
     * 任务二：对外提供获取实例对象的接口（根据id获取）
     */

    private static Map<String,Object> map = new HashMap<>();  // 存储对象 id:object

    private static Map<String, String> class2IdMap = new HashMap<>(); // className:id

    static {

        try {

            Reflections reflections = new Reflections(new ConfigurationBuilder()
                                            .setUrls(ClasspathHelper.forPackage("com.lagou.edu"))
                                            .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(),
                                                    new FieldAnnotationsScanner()));

            // Scan @MyService(id)
            Set<Class<?>> myServiceTypes = reflections.getTypesAnnotatedWith(MyService.class);
            System.out.println("# of @MyService = " + myServiceTypes.size());
            for (Class<?> myServiceType : myServiceTypes) {
                MyService annotation = myServiceType.getAnnotation(MyService.class);
                putAnnotatedObjIntoMaps(annotation.value(), myServiceType);
            }

            // Scan @MyComponent(id)
            Set<Class<?>> myComponentTypes = reflections.getTypesAnnotatedWith(MyComponent.class);
            System.out.println("# of @MyComponent = " + myComponentTypes.size());
            for (Class<?> myComponentType : myComponentTypes) {
                MyComponent annotation = myComponentType.getAnnotation(MyComponent.class);
                putAnnotatedObjIntoMaps(annotation.value(), myComponentType);
            }

            // Scan @MyAutowired(id)
            Set<Field> myAutowiredFields = reflections.getFieldsAnnotatedWith(MyAutowired.class);
            System.out.println("# of @MyAutowired relationship = " + myAutowiredFields.size());
            for (Field field : myAutowiredFields) {
                String parentClassName = field.getDeclaringClass().getName();
                System.out.println("Current parent class = " + parentClassName);
                if (class2IdMap.containsKey(parentClassName)) {
                    String parentId = class2IdMap.get(parentClassName);
                    Object parentObj = map.get(parentId);

                    MyAutowired annotation = field.getAnnotation(MyAutowired.class);
                    String autoId = annotation.value();

                    System.out.println("Current autoId = " + autoId);

                    String filedName = field.getName();
                    System.out.println("Current field name = " + filedName);

                    Field[] fields = parentObj.getClass().getDeclaredFields();
                    for (Field f : fields) {

                        if (f.getName().equals(filedName)) {
                            System.out.println("before set field " + filedName
                                    + ", f getname = " + f.getDeclaringClass() + "===" + f.getType().getName());

                            /*
                            * Check ambiguities -- it is NOT allowed that
                            * there are 2+ implementation classes annotated with
                            * @MyComponent without value. Value must be specified
                            * under this situation.
                             * */
                            String currentAutoClassName = f.getType().getName();
                            Object autoObj = null;
                            if (autoId.isEmpty()) {
                                if (class2IdMap.containsKey(currentAutoClassName)) {
                                    autoObj = map.get(class2IdMap.get(currentAutoClassName));
                                }else {
                                    throw new IllegalArgumentException("请注明@Autowired的value值。");
                                }
                            }else {
                                autoObj = map.get(autoId);
                            }

                            f.setAccessible(true);
                            f.set(parentObj, autoObj);

                            System.out.println("after set field " + filedName + ", f = " + f);
                        }
                    }

                    map.put(parentId, parentObj);
                }

            }

            // Generate Proxy object
            // Scan @MyTransactional
            Set<Class<?>> myTransactionalTypes = reflections.getTypesAnnotatedWith(MyTransactional.class);
            System.out.println("# of @MyTransactional = " + myTransactionalTypes.size());
            for (Class<?> myTransactionalType : myTransactionalTypes) {
                MyTransactional annotation = myTransactionalType.getAnnotation(MyTransactional.class);
                boolean useJDK = annotation.useJDK();
                String clazz = myTransactionalType.getName(); // com.lagou.edu.service.impl.MyServiceImpl
                String id = class2IdMap.get(clazz);
                Object originObj = map.get(id);

                Object proxyObj = generateProxyObj(originObj, useJDK);

                map.put(id, proxyObj);
            }

        // printMaps();

        } catch ( ClassNotFoundException | IllegalAccessException
                | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    // 任务二：对外提供获取实例对象的接口（根据id获取）
    public static Object getBean(String id) {
        return map.get(id);
    }

    private static Object generateProxyObj(Object origin, boolean useJDK) throws InvocationTargetException, IllegalAccessException {
        String proxyName = useJDK ? "JDK" : "CGLIB";
        Object proxyFactory = map.get("proxyFactory");
        Object result = origin;

        Method[] methods = proxyFactory.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getName().equalsIgnoreCase("get" + proxyName + "Proxy")) {
               return method.invoke(proxyFactory, origin);
            }
        }
        // throw new IllegalArgumentException("ProxyFactory不能正常生产代理对象！");
        return origin; // return origin 方便测试
    }

    private static void putAnnotatedObjIntoMaps(String id, Class<?> myType) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String clazz = myType.getName();
        Class<?> aClass = Class.forName(clazz);
        Object o = aClass.newInstance();

        map.put(id, o);
        class2IdMap.put(clazz, id);
    }

    private static void printMaps() {
        for (String key : map.keySet()) {
            System.out.println("map:  " + key + " ---> " + map.get(key));
        }
        System.out.println();
        for (String key : class2IdMap.keySet()) {
            System.out.println("class2IdMap:  " + key + " ---> " + class2IdMap.get(key));
        }
    }
}
