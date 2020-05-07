package com.lagou.edu.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {

    // 由于使用了beans.xml来管理对象生成和对象之间的依赖关系，故这里不再需要将ConnectionUtils做成单例。

    // To store the connection of current thread
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    /*
    * Get a connection object from current thread
    * */
    public Connection getConnectionFromCurrentThread() throws SQLException {
        /*
        * 判断current thread是否已经绑定链接，若没有，则需要从连接池里去一个connection
        * */
        Connection connection = threadLocal.get();
        if (connection == null) {
            // Get a connection from pool and bind it to current thread
            connection = DruidUtils.getInstance().getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }
}
