package com.lagou.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"com.lagou.edu"})
@Import(JdbcConfiguration.class)  // 引入自定义JdbcConfiguration类
@EnableTransactionManagement(proxyTargetClass = false)  // default is false, meaning JDK proxy; true is cglib proxy
public class SpringConfiguration {

    @Autowired
    DataSource dataSource;

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}
