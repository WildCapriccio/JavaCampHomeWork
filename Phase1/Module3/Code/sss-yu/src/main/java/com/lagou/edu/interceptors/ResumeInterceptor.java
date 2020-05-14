package com.lagou.edu.interceptors;

import com.lagou.edu.pojo.Account;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResumeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("ResumeInterceptor preHandle......");

        Account account = (Account) request.getSession().getAttribute("LOGGEDIN_USER");

        if (account == null || !"admin".equals(account.getName()) || !"admin".equals(account.getPassword())) {
            response.sendRedirect("/login.failed");
            return false;
        }

        return true;
    }

}
