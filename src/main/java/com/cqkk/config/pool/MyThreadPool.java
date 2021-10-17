package com.cqkk.config.pool;

import java.util.concurrent.*;

//流程:
//主线程通过execute创建一个线程，发现corePool为空，则直接放入corePool
//如果corePool满了，其余线程则进入workQueue等待，如果workQueue也满了
//如果workQueue也满了，则执行扩容机制，扩容至最大线程数maximumPoolSize
//如果maximumPoolSize也满了，则执行拒绝策略
//当线程数逐渐少了下来之后，keepAliveTime时间到之后，则会清除多余空闲线程
//拒绝策略:
//AbortPolicy:ThreadPoolExecutor中默认的拒绝策略就是AbortPolicy。直接抛出异常也不处理
//CallerRunsPolicy:CallerRunsPolicy在任务被拒绝添加后，会调用当前线程池的所在的线程去执行被拒绝的任务。
//DiscardPolicy:该策略默默地丢弃无法处理的任务，不会抛异常也不会执行，如果允许任务丢失，则这是最好的策略。
//DiscardOldestPolicy:DiscardOldestPolicy策略的作用是，当任务被拒绝添加时，会抛弃任务队列中最旧的任务也就是最先加入队列的，再把这个新任务添加进去。
public class MyThreadPool {
    public static void main(String[] args) {
        //int corePoolSize：线程池中的常驻核心线程数
        //int maximumPoolSize：线程池中能够容纳同时执行的最大线程数，此值一定大于等于1
        //long keepAliveTime：多余空闲线程的存活时间
        //TimeUnit unit：long keepAliveTime的单位
        //BlockingQueue workQueue：任务队列，被提交但尚未被执行的任务
        //ThreadFactory threadFactory：表示生成线程池中工作线程的线程工厂，拥有创建线程，一般默认即可
        //handler：拒绝策略，当队列满了之后，并且工作线程大于corePoolSize最大线程数，时来拒绝请求执行的runnale策略
        ExecutorService threadPool = new ThreadPoolExecutor(
                //corePoolSize
                2,
                //maximumPoolSize
                3,
                //keepAliveTime
                2L,
                //unit
                TimeUnit.SECONDS,
                //workQueue
                new LinkedBlockingDeque<>(3),
                //threadFactory
                Executors.defaultThreadFactory(),
                //handler
                new ThreadPoolExecutor.AbortPolicy());
        try {
            for (int i = 0; i < 10; i++) {
                //Lambda表达式，因为参数对象是Runnable函数式接口，可以使用
                final int index = i;
                threadPool.execute(() -> {
                    System.out.println(index + "=====>" + Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println((index + "完毕"));
                });
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            //开启线程池后需要关闭
            threadPool.shutdown();
        }
    }
}
