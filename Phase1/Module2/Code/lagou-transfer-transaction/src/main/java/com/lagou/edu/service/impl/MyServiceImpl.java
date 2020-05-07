package com.lagou.edu.service.impl;

import com.lagou.edu.annotation.MyAutowired;
import com.lagou.edu.annotation.MyService;
import com.lagou.edu.annotation.MyTransactional;
import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.service.TransferService;

@MyService(value = "myTransferService")
@MyTransactional
public class MyServiceImpl implements TransferService {

    @MyAutowired(value = "myDao")
    private AccountDao myDaoImpl;

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {
        myDaoImpl.queryAccountByCardNo("123");
    }

}
