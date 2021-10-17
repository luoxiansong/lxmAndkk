package com.cqkk.config.juc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
//ForkJoin:一个用于并行执行任务的框架， 是一个把大任务分割成若干个小任务，最终汇总每个小任务结果后得到大任务结果的框架，主要用于并行计算中
//思想：分治，fork分解任务，join收集数据
//Java标准库提供的java.util.Arrays.parallelSort(array)可以进行并行排序，它的原理就是内部通过Fork/Join对大数组分拆进行并行排序，在多核CPU上就可以大大提高排序的速度。

//ForkJoinPool
//ForkJoin 使用 ForkJoinPool 来启动，它是一个特殊的线程池，线程数量取决于 CPU 核数
//ForkJoinPool 实现了工作窃取算法来提高 CPU 的利用率。每个线程都维护了一个双端队列，用来存储需要执行的任务。
// 工作窃取算法允许空闲的线程从其它线程的双端队列中窃取一个任务来执行。窃取的任务必须是最晚的任务，避免和队列所属线程发生竞争。
// 例如下图中，Thread2 从 Thread1 的队列中拿出最晚的 Task1 任务，Thread1 会拿出Task2 来执行，这样就避免发生竞争。但是如果队列中只有一个任务时还是会发生竞争。

//ForkJoinTask
//ForkJoinTask就是ForkJoinPool里面的每一个任务。他主要有两个子类：RecursiveAction和RecursiveTask。然后通过fork()方法去分配任务执行任务，通过join()方法汇总任务结果，
public class JUCForkJoinPool {

    private static final Integer MAX = 10000;

    static class MyForkJoinTask extends RecursiveTask<Integer> {
        //子任务开始计算的值
        private Integer startValue;
        //子任务结束计算的值
        private Integer endValue;

        public MyForkJoinTask(Integer startValue, Integer endValue) {
            this.endValue = endValue;
            this.startValue = startValue;
        }

        @Override
        protected Integer compute() {
            //如果条件成立,说明这个任务所需要计算的数值分为比较小了
            //可以正式进行累加计算了
            if (endValue - startValue < MAX) {
                System.out.println(("开始计算的值=》" + startValue + ";结束计算的值为" + endValue));
                int totalSum = 0;
                for (int i = startValue; i < endValue; i++) {
                    totalSum += i;
                }
                return totalSum;
            } else {
                MyForkJoinTask myForkJoinTask = new MyForkJoinTask(startValue, (endValue + startValue) / 2);
                myForkJoinTask.fork();
                MyForkJoinTask myForkJoinTask1 = new MyForkJoinTask((endValue + startValue) / 2 + 1, endValue);
                myForkJoinTask1.fork();
                return myForkJoinTask.join() + myForkJoinTask1.join();
            }
        }
    }

    public static void main(String[] args) {
        //这是Fork/Join框架的线程池
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> taskFuture = forkJoinPool.submit(new MyForkJoinTask(1, 10001));
        try {
            Integer result = taskFuture.get();
            System.out.println(("最后的执行结果为=>" + result));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
