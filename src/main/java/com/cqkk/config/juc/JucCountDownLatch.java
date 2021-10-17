package com.cqkk.config.juc;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//countDownLatch这个类使一个线程等待其他线程各自执行完毕后再执行。是通过一个计数器来实现的，计数器的初始值是线程的数量。
//每当一个线程执行完毕后，计数器的值就-1，当计数器的值为0时，表示所有线程都执行完毕，然后在闭锁上等待的线程就可以恢复工作了。
public class JucCountDownLatch {
    //调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行
    //public void await() throws InterruptedException { };

    //和await()类似，只不过等待一定的时间后count值还没变为0的话就会继续执行
    //public boolean await(long timeout, TimeUnit unit) throws InterruptedException { };

    //将count值减1
    //public void countDown() { };
    public static void main(String[] args) {
//        CountdownLatchTest1();
        CountdownLatchTest2();
    }

    //CountDownLatch典型用法：1、某一线程在开始运行前等待n个线程执行完毕。
    // 将CountDownLatch的计数器初始化为new CountDownLatch(n)，每当一个任务线程执行完毕，就将计数器减1 countdownLatch.countDown()，
    // 当计数器的值变为0时，在CountDownLatch上await()的线程就会被唤醒。一个典型应用场景就是启动一个服务时，主线程需要等待多个组件加载完毕，之后再继续执行。

    //主线程等待子线程执行完成在执行
    public static void CountdownLatchTest1() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        final CountDownLatch latch = new CountDownLatch(4);
        for (int i = 0; i < 4; i++) {
            Runnable runnable = () -> {
                try {
                    System.out.println(("子进程" + Thread.currentThread().getName() + "开始执行"));
                    Thread.sleep((long) (Math.random() * 10000));
                    System.out.println(("子进程" + Thread.currentThread().getName() + "执行结束"));
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            executorService.execute(runnable);
        }
        try {
            System.out.println(("主进程" + Thread.currentThread().getName() + "等待子进程执行完成"));
            latch.await();
            System.out.println(("主进程" + Thread.currentThread().getName() + "开始执行"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    //CountDownLatch典型用法：2、实现多个线程开始执行任务的最大并行性。注意是并行性，不是并发，强调的是多个线程在某一时刻同时开始执行。
    // 类似于赛跑，将多个线程放到起点，等待发令枪响，然后同时开跑。
    // 做法是初始化一个共享的CountDownLatch(1)，将其计算器初始化为1，多个线程在开始执行任务前首先countdownlatch.await()，
    // 当主线程调用countDown()时，计数器变为0，多个线程同时被唤醒

    //百米赛跑，4名运动员选手到达场地等待裁判口令，裁判一声口令，选手听到后同时起跑，当所有选手到达终点，裁判进行汇总排名
    public static void CountdownLatchTest2() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch cdOrder = new CountDownLatch(1);
        CountDownLatch cdAnswer = new CountDownLatch(4);
        for (int i = 0; i < 4; i++) {
            Runnable runnable = () -> {
                try {
                    System.out.println("选手" + Thread.currentThread().getName() + "正在等待裁判发令");
                    cdOrder.await();
                    System.out.println(("选手" + Thread.currentThread().getName() + "已经接受到裁判号令"));
                    Thread.sleep((long) (Math.random() * 10000));
                    System.out.println(("选手" + Thread.currentThread().getName() + "到达终点"));
                    cdAnswer.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            executorService.execute(runnable);
        }
        try {
            Thread.sleep((long) (Math.random() * 10000));
            System.out.println(("裁判" + Thread.currentThread().getName() + "即将发布口令"));
            cdOrder.countDown();
            System.out.println(("裁判" + Thread.currentThread().getName() + "已发送口令,正在等待所有选手到达终点"));
            cdAnswer.await();
            System.out.println(("所有选手已经到达终点"));
            System.out.println(("裁判" + Thread.currentThread().getName() + "汇报成绩排名"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

}
