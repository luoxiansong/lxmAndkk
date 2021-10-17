package com.cqkk.config.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

//Semaphore也是一个线程同步的辅助类，可以维护当前访问自身的线程个数，并提供了同步机制。使用Semaphore可以控制同时访问资源的线程个数，例如，实现一个文件允许的并发访问数。
public class JUCSemaphore {
    //Semaphore的主要方法摘要:
    //　　void acquire():从此信号量获取一个许可，在提供一个许可前一直将线程阻塞，否则线程被中断。
    //　　void release():释放一个许可，将其返回给信号量。
    //　　int availablePermits():返回此信号量中当前可用的许可数。
    //　　boolean hasQueuedThreads():查询是否有线程正在等待获取。
    public static void main(String[] args) {
        semaphoreTest1();
    }

    public static void semaphoreTest1() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(3);//创建Semaphore信号量,初始化大小为3
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep((long) (Math.random() * 10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Runnable runnable = () -> {
                try {
                    ////请求获得许可，如果有可获得的许可则继续往下执行，许可数减1。否则进入阻塞状态
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(("线程" + Thread.currentThread().getName() + "进入，当前已有" + (3 - semaphore.availablePermits()) + "个并发"));
                try {
                    Thread.sleep((long) (Math.random() * 10000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(("线程" + Thread.currentThread().getName() + "离开"));
                semaphore.release();
            };
            executorService.execute(runnable);
        }
        executorService.shutdown();
    }

}

