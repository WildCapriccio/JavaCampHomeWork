package com.lagou.edu.dao.impl;

import com.lagou.edu.annotation.MyAutowired;
import com.lagou.edu.annotation.MyComponent;
import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.pojo.Account;
import com.lagou.edu.utils.MyUtil;

@MyComponent(value = "myDao")
public class MyDaoImpl implements AccountDao {

    @MyAutowired(value = "myUtil")
    private MyUtil myUtil;

    @Override
    public Account queryAccountByCardNo(String cardNo) throws Exception {
        System.out.println("Test my autowired annotation successful! ");
        myUtil.print();
        return null;
    }

    @Override
    public int updateAccountByCardNo(Account account) throws Exception {
        return 0;
    }
}
