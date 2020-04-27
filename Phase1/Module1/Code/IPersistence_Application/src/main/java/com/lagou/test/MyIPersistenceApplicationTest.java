package com.lagou.test;

import com.lagou.io.Resources;
import com.lagou.mapper.IUserMapper;
import com.lagou.pojo.User;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class MyIPersistenceApplicationTest {

    private IUserMapper userMapper;

    @Before
    public void before() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        userMapper = sqlSession.getMapper(IUserMapper.class);
    }

    /*
     * Select 功能
     * */
    @Test
    public void testFindAll() throws Exception{
        List<User> all = userMapper.findAll();
        for (User user : all) {
            System.out.println(user);
        }
    }

    @Test
    public void testFindByCondition() throws Exception {
        User user = new User();
        user.setId(2);
        user.setUsername("Lisa");
        System.out.println(userMapper.findByCondition(user));
    }

    @Test
    public void testSelectUserById() throws Exception {
        int id = 1;
        System.out.println(userMapper.findUserById(id));
    }

    /*
     * Add 功能
     * */
    @Test
    public void testAddUser() throws Exception {
        User user = new User();
        user.setId(4);
        user.setUsername("新成员");

        userMapper.addUser(user);
    }

    /*
    * Delete 功能
    * */
    @Test
    public void testDeleteUser() throws Exception {
        int id = 4;
        userMapper.deleteUser(id);
    }

    /*
     * Update 功能
     * */
    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setId(3);
        user.setUsername("Tom");

        userMapper.updateUser(user);
    }
}
