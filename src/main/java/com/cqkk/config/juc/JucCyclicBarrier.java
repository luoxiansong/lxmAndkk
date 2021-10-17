package com.cqkk.config.juc;


import java.util.concurrent.CyclicBarrier;

//用来控制多个线程互相等待，只有当多个线程都到达时，这些线程才会继续执行。
//和 CountdownLatch 相似，都是通过维护计数器来实现的。线程执行 await() 方法之后计数器会减 1，并进行等待，直到计数器为 0，所有调用 await() 方法而在等待的线程才能继续执行。
//它的作用就是会让所有线程都等待完成后才会继续下一步行动。
public class JucCyclicBarrier {

    static class TaskThread extends Thread {

        CyclicBarrier barrier;

        public TaskThread(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(getName() + " 到达栅栏 A");
                barrier.await();
                System.out.println(getName() + " 冲破栅栏 A");

                Thread.sleep(2000);
                System.out.println(getName() + " 到达栅栏 B");
                barrier.await();
                System.out.println(getName() + " 冲破栅栏 B");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //构造方法
    //public CyclicBarrier(int parties)
    //public CyclicBarrier(int parties, Runnable barrierAction)
    //parties 是参与线程的个数
    //第二个构造方法有一个Runnable参数，这个参数的意思是最后一个到达线程要做的任务

    //重要方法
    //public int await() throws InterruptedException, BrokenBarrierException
    //public int await(long timeout, TimeUnit unit) throws InterruptedException, BrokenBarrierException, TimeoutException

    //从打印结果可以看出，所有线程会等待全部线程到达栅栏之后才会继续执行，并且最后到达的线程会完成 Runnable 的任务。
    //应用:可以用于多线程计算数据，最后合并计算结果的场景。
    //CyclicBarrier 与 CountDownLatch 区别:
    //CountDownLatch 是一次性的，CyclicBarrier 是可循环利用的
    //CountDownLatch 参与的线程的职责是不一样的，有的在倒计时，有的在等待倒计时结束。CyclicBarrier 参与的线程职责是一样的。
    public static void main(String[] args) {
        int threadNum = 5;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNum, () -> {
            System.out.println((Thread.currentThread().getName() + "完成最后任务"));
        });
        for (int i = 0; i < 5; i++) {
            new Thread(new TaskThread(cyclicBarrier)).start();
        }
    }
}
