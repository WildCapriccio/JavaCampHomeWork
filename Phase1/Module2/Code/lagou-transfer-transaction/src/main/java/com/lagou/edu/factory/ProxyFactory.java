package com.lagou.edu.factory;

import com.lagou.edu.pojo.Account;
import com.lagou.edu.utils.TransactionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {

    // 不再单例化，因为beans.xml会通过反射机制生成ProxyFactory对象，其背后会用到ProxyFactory的无参constructor

    // 用beans.xml来管理class之间的依赖关系
    private TransactionManager transactionManager;
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Object getJDKProxy(Object obj) {
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object result = null;

                        try {
                            // 开启事务，务必关闭事务的自动提交
                            transactionManager.beginTransaction();

                            result = method.invoke(obj, args);   // !!! 业务逻辑见实现类中代码

                            // 提交事务
                            transactionManager.commit();

                        } catch (Exception e) {
                            e.printStackTrace();
                            // 回滚事务，由于出现了异常
                            transactionManager.rollback();

                            // 抛出异常，便于上层（servlet）捕获。
                            // 如果这里不抛，虽然后台依然会报异常，但servlet由于拿不到异常，就会在UI上显示"转账成功"
                            throw e;
                        }

                        return result;
                    }
                });
    }
}