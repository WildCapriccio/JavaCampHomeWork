package com.lagou.edu.interceptors;

import com.lagou.edu.pojo.Account;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // A Simple Authentication
        Map<String, Object> model = modelAndView.getModel();
        Account account = (Account) model.get("account");

//        if (account == null || !"admin".equals(account.getName()) || !"admin".equals(account.getPassword())) {
//            response.sendRedirect("/login.failed");   // 报错：Cannot call sendRedirect() after the response has been committed
//        }else {
//            request.getSession().setAttribute("LOGGEDIN_USER", account);
//        }

        request.getSession().setAttribute("LOGGEDIN_USER", account);
    }
}
