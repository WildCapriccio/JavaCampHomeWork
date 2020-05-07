package com.lagou.edu.utils;

import java.sql.SQLException;

/*
* 事务管理器类：负责手动的对事务进行开启、提交、回滚。
* */
public class TransactionManager {

    // 由于使用了beans.xml来管理对象生成和对象之间的依赖关系，故这里不再需要将TransactionManager做成单例。

    // 用beans.xml来管理class之间的依赖关系
    private ConnectionUtils connectionUtils;
    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

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
