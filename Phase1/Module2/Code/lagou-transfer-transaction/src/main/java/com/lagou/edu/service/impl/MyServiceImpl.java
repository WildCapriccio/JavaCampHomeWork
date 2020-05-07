package com.lagou.edu.service.impl;

import com.lagou.edu.annotation.MyService;
import com.lagou.edu.service.TransferService;

@MyService(value = "myTransferService")
public class MyServiceImpl implements TransferService {

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

    }
}
