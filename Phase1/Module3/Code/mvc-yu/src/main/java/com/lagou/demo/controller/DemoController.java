package com.lagou.demo.controller;

import com.lagou.demo.service.IDemoService;
import com.lagou.edu.mvcframework.annotations.YuAutowired;
import com.lagou.edu.mvcframework.annotations.YuController;
import com.lagou.edu.mvcframework.annotations.YuRequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@YuController
@YuRequestMapping("/demo")
public class DemoController {

    @YuAutowired
    private IDemoService demoService;

    /**
     * URL = /demo/query
     * */
    @YuRequestMapping("/query")
    public String query(HttpServletRequest request, HttpServletResponse response, String name) throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print("Success from DemoController!");

        out.close();

        return demoService.get(name);
    }
}
