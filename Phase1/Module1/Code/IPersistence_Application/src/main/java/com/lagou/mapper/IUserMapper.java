package com.lagou.mapper;

import com.lagou.pojo.User;

import java.util.List;

public interface IUserMapper {

    /*
    * Select 功能
    * */
    // 根据id查找用户
    public User findUserById(Integer id) throws Exception;

    public List<User> findAll() throws Exception;

    public User findByCondition(User user) throws Exception;

    /*
     * Add 功能
     * */
    public void addUser(User user) throws Exception;

    /*
     * Delete 功能
     * */
    public void deleteUser(Integer id) throws Exception;

    /*
     * Update 功能
     * */
    public void updateUser(User user) throws Exception;
}
