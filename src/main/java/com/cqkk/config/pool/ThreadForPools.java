package com.cqkk.config.pool;

public class ThreadForPools implements Runnable {

    private Integer index;

    public ThreadForPools(Integer index) {
        this.index = index;
    }

    @Override
    public void run() {
        System.out.println("线程开始执行" + Thread.currentThread().getName());
        try {
            Thread.sleep(index * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程标识为=====>index" + index + Thread.currentThread().getName());
    }
}
