package com.cqkk.config.juc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

//FutureTask 实现了 RunnableFuture 接口，该接口继承自 Runnable 和 Future 接口，这使得 FutureTask 既可以当做一个任务执行，也可以有返回值。
public class JucFutureTask {
    public static void main(String[] args) {
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            int result = 0;
            for (int i = 0; i < 1000; i++) {
                Thread.sleep((long) (Math.random() * 1000));
                result += i;
            }
            return result;
        }
        );
        Thread thread = new Thread(futureTask);
        thread.start();
        Thread otherThread = new Thread(() -> {
            System.out.println("other task is running...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        otherThread.start();
        try {
            System.out.println(futureTask.get());//获取返回值
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
