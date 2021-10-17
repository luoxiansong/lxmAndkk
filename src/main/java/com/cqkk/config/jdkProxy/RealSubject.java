package com.cqkk.config.jdkProxy;

/*真实主题类*/
public class RealSubject implements Subject {
    @Override
    public void doSomething() {
        System.out.println("RealSubject do something");
    }
}
