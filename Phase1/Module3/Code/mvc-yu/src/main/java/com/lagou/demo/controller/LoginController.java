package com.lagou.demo.controller;

import com.lagou.edu.mvcframework.annotations.YuController;
import com.lagou.edu.mvcframework.annotations.YuRequestMapping;
import com.lagou.edu.mvcframework.annotations.YuSecurity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@YuController
@YuRequestMapping("/account")
public class LoginController {

    /**
     * URL = account/login
     * */
    @YuRequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response, String username) throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print("Hello, " + username);

        out.close();
    }

    /**
     * URL = account/admin/login
     * */
    @YuSecurity({"Jenny","Lisa"})
    @YuRequestMapping("/admin/login")
    public void adminLogin(HttpServletRequest request, HttpServletResponse response, String username) throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print("Hello, " + username);

        out.close();
    }
}
