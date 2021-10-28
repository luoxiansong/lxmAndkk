package com.cqkk.config.jdk8.Async;

import java.util.concurrent.*;

/**
 * jdk1.8之前的Future
 *
 * @author Administrator
 */
public class JavaFuture {
    public static void main(String[] args) throws Throwable, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<String> f = executor.submit(new Callable<String>() {

            @Override
            public String call() throws Exception {
                System.out.println("task started!");
                Thread.sleep(4000);
                System.out.println("task finished!");
                return "hello";
            }
        });

        //此处get()方法阻塞main线程
        System.out.println(f.get());
        System.out.println("main thread is blocked");
    }
}
