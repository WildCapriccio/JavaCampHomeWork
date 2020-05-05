package com.lagou.edu.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * 注解@Transactional可以加在Interface上（会影响所有实现类）
 * 或者加在特定实现类上
 * 或者加在某个特定方法上 【优先级最高】
 *
 * 一般加在实现类上即可。
 */
public interface TransferService {

    void transfer(String fromCardNo,String toCardNo,int money) throws Exception;
}
