package com.cqkk.config.jdkProxy;

/*测试jdk动态代理*/
public class JdkProxyClient {
    public static void main(String[] args) {
        Subject subject = new JDKDynamicProxy(new RealSubject()).getProxy();
        subject.doSomething();
    }
}
