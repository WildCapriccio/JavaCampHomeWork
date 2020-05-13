package com.lagou.edu.mvcframework.pojo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/*
* 封装Handler方法的各种相关信息
* */
public class Handler {

    private Object controller;  // 即方法所属于的那个类的 bean对象

    private Method method;

    private Pattern pattern;  // spring 中 url是支持正则的

    private List<Object> interceptors;

    private Map<String, Integer> paramIndexMapping;  // 参数名:第几个参数（从0开始），方便进行参数绑定

    public Handler(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        this.paramIndexMapping = new HashMap<>();
        this.interceptors = new LinkedList<>();
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Map<String, Integer> getParamIndexMapping() {
        return paramIndexMapping;
    }

    public void setParamIndexMapping(Map<String, Integer> paramIndexMapping) {
        this.paramIndexMapping = paramIndexMapping;
    }

    public List<Object> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<Object> interceptors) {
        this.interceptors = interceptors;
    }
}
