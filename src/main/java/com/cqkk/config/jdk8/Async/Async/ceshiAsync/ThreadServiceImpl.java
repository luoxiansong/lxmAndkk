package com.cqkk.config.jdk8.Async.Async.ceshiAsync;

import com.google.common.collect.Maps;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author liuyuan
 * @version ThreadServiceImpl.java, v 0.1 2019-11-01 11:06
 */
@Service
public class ThreadServiceImpl implements ThreadService {

    @Resource
    private ThreadPoolTaskConfig threadPoolTaskConfig;

    @Override
    // 线程池1
    @Async("taskExecutor1")
    public void doTaskA() throws InterruptedException {
        System.out.println("TaskA thread name->" + Thread.currentThread().getName());
        Long startTime = System.currentTimeMillis();
        TimeUnit.SECONDS.sleep(2);

        Long endTime = System.currentTimeMillis();
        System.out.println("TaskA 耗时：" + (endTime - startTime));
    }

    @Override
    // 线程池2
    @Async("taskExecutor2")
    public void doTaskB() throws InterruptedException {
        System.out.println("TaskB thread name->" + Thread.currentThread().getName());
        Long startTime = System.currentTimeMillis();
        TimeUnit.SECONDS.sleep(2);
        Long endTime = System.currentTimeMillis();
        System.out.println("TaskB耗时：" + (endTime - startTime));
    }

    @Override
    @Async("taskExecutor3")
    public Future<Map<String, String>> doTaskC() throws InterruptedException {

        System.out.println("TaskC thread name->" + Thread.currentThread().getName());
        Long startTime = System.currentTimeMillis();
        TimeUnit.SECONDS.sleep(2);
        Long endTime = System.currentTimeMillis();
        System.out.println("TaskC耗时：" + (endTime - startTime));

        Map<String, String> map = Maps.newHashMap();
        map.put("return", "返回值是一个map！");
        return new AsyncResult<>(map);
    }

    @Override
    public FutureTask<String> doTaskD() {
        Long startTime = System.currentTimeMillis();
        FutureTask<String> future = new FutureTask<>(() -> {
            TimeUnit.SECONDS.sleep(2);
            Long endTime = System.currentTimeMillis();
            System.out.println("TaskC耗时：" + (endTime - startTime));
            return "成功获取future异步任务结果,threadName = " + Thread.currentThread().getName();
        });

        Executor pool = threadPoolTaskConfig.taskExecutor3();
        pool.execute(future);
        return future;
    }

    //CompletableFuture 进行变换
    @Override
    public void doTaskG() {
        String result = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程1" + Thread.currentThread().getName());
            return "hello";
        }, threadPoolTaskConfig.taskExecutor1()).thenApplyAsync(s -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程2" + Thread.currentThread().getName());
            return s + "dosomething";
        }, threadPoolTaskConfig.taskExecutor2()).join();
        System.out.println("return: " + result);
    }

    //CompletableFuture 进行消耗
    @Override
    public void doTaskH() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("线程1" + Thread.currentThread().getName());
            return "CompletableFuture";
        }, threadPoolTaskConfig.taskExecutor1()).thenAcceptAsync(s -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程2" + Thread.currentThread().getName());
            System.out.println("return: " + s + "===" + "thenAcceptAsync");
        }, threadPoolTaskConfig.taskExecutor2());
    }


}
