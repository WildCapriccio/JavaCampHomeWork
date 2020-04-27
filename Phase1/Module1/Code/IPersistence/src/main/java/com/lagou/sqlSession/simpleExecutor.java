package com.lagou.sqlSession;


import com.lagou.config.BoundSql;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.utils.GenericTokenParser;
import com.lagou.utils.ParameterMapping;
import com.lagou.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class simpleExecutor implements  Executor {

    @Override
    public int update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        PreparedStatement preparedStatement = getPreparedStatement(configuration, mappedStatement, params);
        return preparedStatement.executeUpdate();
    }

    @Override                                                                                //user，query方法被调用时候传入
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        PreparedStatement preparedStatement = getPreparedStatement(configuration, mappedStatement, params);

        // 5. 执行sql
        ResultSet resultSet = preparedStatement.executeQuery();

        // 6. 封装返回结果集
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);

        ArrayList<Object> objects = new ArrayList<>();

        while (resultSet.next()){
            // 创建一个resultType类的对象o
            Object o =resultTypeClass.newInstance();
            //元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            // metaData.getColumnCount() 会返回table中有多少列
            for (int i = 1; i <= metaData.getColumnCount(); i++) {

                // 字段名
                String columnName = metaData.getColumnName(i);  // 从1开始
                // 字段的值
                Object value = resultSet.getObject(columnName);

                //使用反射或者内省，根据数据库表和实体的对应关系，完成封装。这里用的内省。
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,value);

            }
            objects.add(o);

        }
        return (List<E>) objects;

    }

    private PreparedStatement getPreparedStatement(Configuration configuration,
                                                   MappedStatement mappedStatement,
                                                   Object... params) throws Exception{
        // 1. 注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();

        // 2. 获取到的sql语句 : select * from user where id = #{id} and username = #{username}
        // 由于 #,{,} 是自定义的占位符，JDBC只认识 ？占位符，所以这里要parse一下，
        // 转换成sql语句： select * from user where id = ? and username = ?
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        // 3.获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        // 4. 设置参数
        //获取到了参数的全路径，是个string （com.lagou.pojo.User）
        String parameterType= mappedStatement.getParameterType();
        // Class<?> 表示class type不确定
        Class<?> parameterTypeClass = getClassType(parameterType);

        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射 - 根据attribute名称来获取某个特定Class的实例中对应attribute的值
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            //暴力访问被允许
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);

            // parameterIndex 是从1开始的
            preparedStatement.setObject(i+1,o);
        }
        return preparedStatement;
    }

    private Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        if(paramterType!=null){
            Class<?> aClass = Class.forName(paramterType);
            return aClass;
        }
        return null;

    }

    /**
     * Util pkg is from MyBats
     * 完成对#{}的解析工作：
     *      1.将#{}使用？进行代替
     *      2.解析出#{}里面的值进行存储
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        //标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql： select * from user where id = ? and username = ?
        String parseSql = genericTokenParser.parse(sql);
        //#{}里面解析出来的参数名称：id, username 等
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(parseSql,parameterMappings);
        return boundSql;
    }

}
