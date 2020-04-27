package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementid, Object... params) throws Exception {

        //将要去完成对simpleExecutor里的query方法的调用
        simpleExecutor simpleExecutor = new simpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementid);
        List<Object> list = simpleExecutor.query(configuration, mappedStatement, params);

        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementid, Object... params) throws Exception {
        List<Object> objects = selectList(statementid, params);
        if(objects.size()==1){
            return (T) objects.get(0);
        }else {
            throw new RuntimeException("查询结果为空或者返回结果过多");
        }
    }

    @Override
    public int insert(String statementid, Object... params) throws Exception {
        return executeUpdate(statementid, params);
    }

    @Override
    public int delete(String statementid, Object... params) throws Exception {
        return executeUpdate(statementid, params);
    }

    @Override
    public int update(String statementid, Object... params) throws Exception {
        return executeUpdate(statementid, params);
    }

    private int executeUpdate(String statementid, Object... params) throws Exception {
        //将要去完成对simpleExecutor里的query方法的调用
        simpleExecutor simpleExecutor = new simpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementid);
        return simpleExecutor.update(configuration, mappedStatement, params);
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        // 使用JDK动态代理来为Mapper接口生成代理对象，并返回

        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                /*
                 * 底层都还是去执行JDBC代码。
                 * 根据不同情况，来调用selctList或者selectOne。但是由于在proxy这里没有办法get到userMapper.xml
                 * 中的namespace 和 id，就不能通过statementId来唯一确定想要执行那条语句。
                 * 所以，为了能够通过 statementId 定位到想要执行的sql语句，这里将userMapper.xml中的
                 * namespace 和 id 的名字分别修改成为 接口全限定名(com.lagou.dao.IUserDao) 和 方法名(findAll / findByCondition)
                 *
                 * 这样，就可以直接用Interface name 和 方法名 来组装 statementId.
                 * */
                // 准备参数 1：statmentid = interface_class_name.method_name

                // 方法名：findAll / findByCondition
                String methodName = method.getName();
                // interface class name
                String className = method.getDeclaringClass().getName();
                String statementId = className+"."+methodName;

                // 准备参数2：params:args
                /*
                * 1. Find sql txt according to statementId in configuration
                * 2. Check first word in sql to identify sql command type
                * 3. According to sql command type choose the appropriate method to execute.
                * */
                Object result = null;
                String sql = configuration.getMappedStatementMap().get(statementId).getSql();
                String sqlCommandType = getSQLCommandType(sql);
                switch (sqlCommandType) {
                    case "insert":
                        result = insert(statementId, args);
                        break;
                    case "delete":
                        result = delete(statementId, args);
                        break;
                    case "update":
                        result = update(statementId, args);
                        break;
                    case "select":
                        // 获取被调用方法的返回值类型
                        Type genericReturnType = method.getGenericReturnType();
                        // 判断是否进行了 泛型类型参数化, return true when genericReturnType是一个集合类型
                        if (genericReturnType instanceof ParameterizedType) {
                            result = selectList(statementId, args);
                        } else {
                            result = selectOne(statementId, args);
                        }
                        break;
                    default:
                        break;
                }
                if (result == null) {
                    throw new RuntimeException("Cannot find this command type.");
                }else {
                    return result;
                }
            }
        });

        return (T) proxyInstance;
    }

    private String getSQLCommandType(String sql) {
        String[] array = sql.split(" ", 2);
        return array[0];
    }
}
