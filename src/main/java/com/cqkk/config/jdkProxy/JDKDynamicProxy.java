package com.cqkk.config.jdkProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*JDK动态代理*/
public class JDKDynamicProxy implements InvocationHandler {

    private Object target;

    public JDKDynamicProxy(Object target) {
        this.target = target;
    }

    /*获取被代理的实例对象*/
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Do something before");
        method.invoke(target, args);
        System.out.println("Do something after");
        return null;
    }
}
