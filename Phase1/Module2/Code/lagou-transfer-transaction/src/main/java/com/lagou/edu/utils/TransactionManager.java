package com.lagou.edu.utils;

import com.lagou.edu.annotation.MyAutowired;
import com.lagou.edu.annotation.MyComponent;

import java.sql.SQLException;

/*
* 事务管理器类：负责手动的对事务进行开启、提交、回滚。
* */
@MyComponent(value = "transactionManager")
public class TransactionManager {

    @MyAutowired
    private ConnectionUtils connectionUtils;

    // 开启手动事务控制
    public void beginTransaction() throws SQLException {
        connectionUtils.getConnectionFromCurrentThread().setAutoCommit(false);
    }

    // 提交事务
    public void commit() throws SQLException {
        connectionUtils.getConnectionFromCurrentThread().commit();
    }

    // 回滚事务
    public void rollback() throws SQLException {
        connectionUtils.getConnectionFromCurrentThread().rollback();
    }
}
