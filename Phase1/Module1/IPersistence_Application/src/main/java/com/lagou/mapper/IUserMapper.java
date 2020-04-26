package com.lagou.mapper;

import com.lagou.pojo.User;

import java.util.List;

public interface IUserMapper {

    // 根据id查找用户
    public User findUserById(Integer id) throws Exception;

    public List<User> findAll() throws Exception;

    public User findByCondition(User user) throws Exception;
}
