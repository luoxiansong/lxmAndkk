package com.cqkk.config.jdk8.Async;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * 基于jdk1.8实现任务异步处理
 */
@Component
public class JavaPromise {

    private static Log log = LogFactory.getLog(JavaPromise.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 两个线程的线程池
        ExecutorService executor = Executors.newFixedThreadPool(2);
        //jdk1.8之前的实现方式
        CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                System.out.println(Thread.currentThread().getName() + "==>task start");
                try {
                    //模拟耗时操作
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return Thread.currentThread().getName() + "==》task finished!";
            }
        }, executor);
        //采用lambada的实现方式
        future.thenAccept(e -> System.out.println(e + "ok"));
        System.out.println("main thread is running");

        //结果:
        //pool-1-thread-1==>task start
        //main thread is running
        //pool-1-thread-1==》task finished!ok
    }
}
