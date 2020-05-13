package com.lagou.edu.mvcframework.interceptors;

import javax.servlet.http.HttpServletRequest;

public interface YuInterceptors {

    boolean preHandle(HttpServletRequest request);
}
