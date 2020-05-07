package com.lagou.edu.factory;

import com.lagou.edu.annotation.MyAutowired;
import com.lagou.edu.annotation.MyComponent;
import com.lagou.edu.annotation.MyService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.InputStream;
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
     * 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
     * 任务二：对外提供获取实例对象的接口（根据id获取）
     */

    private static Map<String,Object> map = new HashMap<>();  // 存储对象 id:object

    private static Map<String, String> class2IdMap = new HashMap<>(); // className:id

    static {
        // 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
        // 加载xml
        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        // 解析xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> beanList = rootElement.selectNodes("//bean");
            // 根据bean标签，实例化所有的bean 对象，并放入map中
            for (int i = 0; i < beanList.size(); i++) {
                Element element =  beanList.get(i);
                // 处理每个bean元素，获取到该元素的id 和 class 属性
                String id = element.attributeValue("id");        // accountDao
                String clazz = element.attributeValue("class");  // com.lagou.edu.dao.impl.JdbcAccountDaoImpl
                // 通过反射技术实例化对象
                Class<?> aClass = Class.forName(clazz);
                Object o = aClass.newInstance();  // 实例化之后的对象

                // 存储到map中待用
                map.put(id,o);

            }

            /*
            * 把各个bean实例化完成后，继续维护bean之间的依赖关系，
            * 即检查哪些对象需要传值进入其他的bean，根据property标签，
            * 来传入相应的bean对象。
            * */
            List<Element> propertyList = rootElement.selectNodes("//property");

            // 解析property，获取父元素
            // <property name="AccountDao" ref="accountDao"></property>
            // trick: itli 可以generate出这个for-loop signature
            for (int i = 0; i < propertyList.size(); i++) {
                Element element =  propertyList.get(i);
                String name = element.attributeValue("name");
                String ref = element.attributeValue("ref");

                // 找到当前property属于哪个bean,即property的直属parent
                Element parent = element.getParent();

                // 找到parent的id，然后找到parent的对象object
                String parentId = parent.attributeValue("id");
                Object parentObject = map.get(parentId);

                // 遍历parent class中的setter，如果找到set+name，就invoke该setter
                Method[] methods = parentObject.getClass().getMethods();
                for (int j = 0; j < methods.length; j++) {
                    Method method = methods[j];
                    if (method.getName().equalsIgnoreCase("set" + name)) {
                        method.invoke(parentObject, map.get(ref));
                    }
                }

                // 记得：把处理后的parentObject重新放回map中
                // 因为被处理后的parentObject已经由于method.invoke而被赋予了新的属性值
                map.put(parentId, parentObject);
            }

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
                    Object autoObj = map.get(autoId);

                    String filedName = field.getName();
                    System.out.println("Current field name = " + filedName);

                    Field[] fields = parentObj.getClass().getDeclaredFields();
                    for (Field f : fields) {
                        System.out.println("parentobj declared fied: " + f.getName());
                        if (f.getName().equals(filedName)) {
                            System.out.println("before set field " + filedName + ", f = " + f);

                            f.setAccessible(true);
                            f.set(parentObj, autoObj);

                            System.out.println("after set field " + filedName + ", f = " + f);
                        }
                    }

                    map.put(parentId, parentObj);

//                    declaredField.setAccessible(true);
//                    declaredField.set(filedName, autoObj);
//
//                    System.out.println("After set, declaredField = " + declaredField);
//                    System.out.println("After set, parentobj = " + parentObj.getClass().getField(filedName));

                }

            }

//            for (String key : map.keySet()) {
//                System.out.println("map:  " + key + " ---> " + map.get(key));
//            }
//
//            for (String key : class2IdMap.keySet()) {
//                System.out.println("class2IdMap:  " + key + " ---> " + class2IdMap.get(key));
//            }


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    // 任务二：对外提供获取实例对象的接口（根据id获取）
    public static Object getBean(String id) {
        return map.get(id);
    }

    private static void putAnnotatedObjIntoMaps(String id, Class<?> myType) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String clazz = myType.getName();
        Class<?> aClass = Class.forName(clazz);
        Object o = aClass.newInstance();

        map.put(id, o);
        class2IdMap.put(clazz, id);
    }
}
