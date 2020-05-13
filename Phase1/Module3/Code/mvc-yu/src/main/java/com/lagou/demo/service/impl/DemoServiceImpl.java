package com.lagou.demo.service.impl;

import com.lagou.demo.service.IDemoService;
import com.lagou.edu.mvcframework.annotations.YuService;

@YuService
public class DemoServiceImpl implements IDemoService {
    @Override
    public String get(String name) {
        System.out.println("Service 实现类中的name参数： " + name);
        return name;
    }
}
