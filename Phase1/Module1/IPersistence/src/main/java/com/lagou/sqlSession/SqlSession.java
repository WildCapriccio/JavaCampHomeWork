package com.lagou.sqlSession;

import java.util.List;

public interface SqlSession {

    //查询所有，用 E 来表示类型不定，保持一致即可，用 ... 表示可变参
    public <E> List<E> selectList(String statementid, Object... params) throws Exception;

    //根据条件查询单个
    public <T> T selectOne(String statementid,Object... params) throws Exception;


    //优化：为Mapper接口生成代理实现类
    public <T> T getMapper(Class<?> mapperClass);
}
