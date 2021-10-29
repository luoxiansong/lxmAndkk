package com.cqkk.config.jdk8.Async;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture
 */
public class CompletableFutureTest {
    public static void main(String[] args) {
        // 任务 1：洗水壶 -> 烧开水
        CompletableFuture<Void> f1 =
                CompletableFuture.runAsync(() -> {
                    System.out.println("T1: 洗水壶...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("T1: 烧开水...");
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        // 任务 2：洗茶壶 -> 洗茶杯 -> 拿茶叶
        CompletableFuture<String> f2 =
                CompletableFuture.supplyAsync(() -> {
                    System.out.println("T2: 洗茶壶...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("T2: 洗茶杯...");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("T2: 拿茶叶...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return " 龙井 ";
                });

        // 任务 3：任务 1 和任务 2 完成后执行：泡茶
        CompletableFuture<String> f3 =
                f1.thenCombine(f2, (__, tf) -> {//任务 3 要等待任务 1 和任务 2 都完成后才能开始
                    System.out.println("T1: 拿到茶叶:" + tf);
                    System.out.println("T1: 泡茶...");
                    return " 上茶:" + tf;
                });
        // 等待任务 3 执行结果
        System.out.println(f3.join());
        // 一次执行结果：
        //T1: 洗水壶...
        //T2: 洗茶壶...
        //T1: 烧开水...
        //T2: 洗茶杯...
        //T2: 拿茶叶...
        //T1: 拿到茶叶: 龙井
        //T1: 泡茶...
        //上茶: 龙井

        /**描述串行关系
         CompletionStage<R> thenApply(fn);
         CompletionStage<R> thenApplyAsync(fn);
         CompletionStage<Void> thenAccept(consumer);
         CompletionStage<Void> thenAcceptAsync(consumer);
         CompletionStage<Void> thenRun(action);
         CompletionStage<Void> thenRunAsync(action);
         CompletionStage<R> thenCompose(fn);
         CompletionStage<R> thenComposeAsync(fn);
         */
        CompletableFuture<String> f0 =
                CompletableFuture.supplyAsync(
                        () -> "Hello World")   //①
                        .thenApply(s -> s + " QQ") //②
                        .thenApply(String::toUpperCase);//③

        System.out.println(f0.join());
        //然这是一个异步流程，但任务①②③却是串行执行的，②依赖①的执行结果，③依赖②的执行结果。
        // 输出结果
        //HELLO WORLD QQ

        /**描述 AND 汇聚关系
         * CompletionStage<R> thenCombine(other, fn);
         * CompletionStage<R> thenCombineAsync(other, fn);
         * CompletionStage<Void> thenAcceptBoth(other, consumer);
         * CompletionStage<Void> thenAcceptBothAsync(other, consumer);
         * CompletionStage<Void> runAfterBoth(other, action);
         * CompletionStage<Void> runAfterBothAsync(other, action);
         */

        /**. 描述 OR 汇聚关系
         * CompletionStage applyToEither(other, fn);
         * CompletionStage applyToEitherAsync(other, fn);
         * CompletionStage acceptEither(other, consumer);
         * CompletionStage acceptEitherAsync(other, consumer);
         * CompletionStage runAfterEither(other, action);
         * CompletionStage runAfterEitherAsync(other, action);
         */
        CompletableFuture<String> f5 =
                CompletableFuture.supplyAsync(() -> {
                    int max = 10;
                    int min = 5;
                    int t = new Random().nextInt(max) % (max - min + 1) + min;
                    System.out.println("f5==>" + t);
                    try {
                        Thread.sleep(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return String.valueOf(t);
                });

        CompletableFuture<String> f6 =
                CompletableFuture.supplyAsync(() -> {
                    int max = 10;
                    int min = 5;
                    int t = new Random().nextInt(max) % (max - min + 1) + min;
                    try {
                        Thread.sleep(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("f6==>" + t);
                    return String.valueOf(t);
                });

        CompletableFuture<String> f7 =
                f5.applyToEither(f6, s -> s);

        System.out.println("f7==>" + f7.join());
    }


}