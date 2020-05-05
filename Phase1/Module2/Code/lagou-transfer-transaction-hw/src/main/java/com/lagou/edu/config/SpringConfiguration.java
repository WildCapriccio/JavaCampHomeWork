package com.lagou.edu.config;

import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan({"com.lagou.edu"})
@Import(JdbcConfiguration.class)  // 引入自定义JdbcConfiguration类
@EnableTransactionManagement
public class SpringConfiguration {


}
