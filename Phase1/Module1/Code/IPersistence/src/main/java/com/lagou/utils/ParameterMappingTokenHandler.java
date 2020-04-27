package com.lagou.utils;

import java.util.ArrayList;
import java.util.List;




public class ParameterMappingTokenHandler implements TokenHandler {
    // 需要自己定义 parameterMapping class
    private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();

    // content 是 #{id} #{username} 里面的 id username

    public String handleToken(String content) {
        parameterMappings.add(buildParameterMapping(content));
        return "?";
    }

    private ParameterMapping buildParameterMapping(String content) {
        ParameterMapping parameterMapping = new ParameterMapping(content);
        return parameterMapping;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }

}
