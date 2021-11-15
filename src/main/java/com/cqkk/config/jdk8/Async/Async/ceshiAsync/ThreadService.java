package com.cqkk.config.jdk8.Async.Async.ceshiAsync;

import org.springframework.scheduling.annotation.Async;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public interface ThreadService {

    // 线程池1
    @Async("taskExecutor1")
    void doTaskA() throws InterruptedException;

    // 线程池2
    @Async("taskExecutor2")
    void doTaskB() throws InterruptedException;

    @Async("taskExecutor3")
    Future<Map<String,String>> doTaskC() throws InterruptedException;

    FutureTask<String> doTaskD();

    void doTaskG();

    void doTaskH();
}
