package com.lagou.edu.dao.impl;

import com.lagou.edu.annotation.MyComponent;
import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.pojo.Account;

@MyComponent(value = "myDao")
public class MyDaoImpl implements AccountDao {

    @Override
    public Account queryAccountByCardNo(String cardNo) throws Exception {
        return null;
    }

    @Override
    public int updateAccountByCardNo(Account account) throws Exception {
        return 0;
    }
}
