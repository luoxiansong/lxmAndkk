package com.cqkk.config.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//除了newScheduledThreadPool之外 其他底层都是用new ThreadPoolExecutor()创建的
//两种创建线程池的方式
//1.是通过Executors工具类(不推荐)
//2.是通过ThreadPoolExecutor来创建(推荐)

//推荐使用ThreadPoolExecutor来创建的理由:更加明确线程池的运行规则,规避资源耗尽的风险
//1>FixedThreadPool和SingleThreadPool:允许的请求队列长度为Integer.MAX_VALUE,可能会堆积大量的请求,从而导致OOM
//2>CachedThreadPool:允许创建线程数量为Integer.MAX_VALUE,可能创建大量的线程,从而导致OOM
public class ThreadPools {

    public static void main(String[] args) {
//        createNewCachedThreadPool();
        createNewFixedThreadPool();
//        createNewScheduledThreadPool();
//        createNewSingleThreadExecutor();
    }

    /*newCachedThreadPool*/
    //newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
    //创建一个可缓存线程池，应用中存在的线程数可以无限大
    //可以有无限大的线程数进来（线程地址不一样），但需要注意机器的性能，需要线程太多，会导致服务器出现问题。
    public static void createNewCachedThreadPool() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println("========newCachedThreadPool==========");
        for (int i = 0; i < 4; i++) {
            final int index = i;
            executorService.execute(new ThreadForPools(index));
        }
    }

    //newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
    //Executors.newFixedThreadPool(2);
    // 在线程池中保持二个线程可以同时执行，但是注意，并不是说线程池中永远都是这二个线程，只是说可以同时存在的线程数，当某个线程执行结束后，会有新的线程进来
    public static void createNewFixedThreadPool() {
        //线程池允许同时存在两个线程
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        System.out.println("========newFixedThreadPool==========");
        for (int i = 0; i < 4; i++) {
            final int index = i;
            executorService.execute(new ThreadForPools(index));
        }
    }

    // newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行
    //newScheduledThreadPool共计有三个方法：
    //schedule(commod,delay,unit) ，这个方法是说系统启动后，需要等待多久执行，delay是等待时间。只执行一次，没有周期性。
    //scheduleAtFixedRate(commod,initialDelay,period,unit)，这个是以period为固定周期时间，按照一定频率来重复执行任务，initialDelay是说系统启动后，需要等待多久才开始执行。例如：如果设置了period为5秒，线程启动之后执行了大于5秒，线程结束之后，立即启动线程的下一次，如果线程启动之后只执行了3秒就结束了那执行下一次，需要等待2秒再执行。这个是优先保证任务执行的频率，
    //scheduleWithFixedDelay(commod,initialDelay,delay,unit)，这个是以delay为固定延迟时间，按照一定的等待时间来执行任务，initialDelay意义与上面的相同。例如：设置了delay为5秒，线程启动之后不管执行了多久，结束之后都需要先生5秒，才能执行下一次。这个是优先保证任务执行的间隔。
    public static void createNewScheduledThreadPool() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        System.out.println("========newScheduledThreadPool==========");
        for (int i = 0; i < 4; i++) {
            final int index = i;
            executorService.schedule(new ThreadForPools(index), 4, TimeUnit.SECONDS);
        }
    }

    //newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
    public static void createNewSingleThreadExecutor() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        System.out.println("========newSingleThreadExecutor==========");
        for (int i = 0; i < 4; i++) {
            final int index = i;
            executorService.execute(new ThreadForPools(index));
        }
    }
}
