package com.cqkk.cqkk.config.Async.Async.ceshiAsync;

import com.cqkk.config.jdk8.Async.Async.ceshiAsync.ThreadService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class AsyncThreadPoolTest {

    @Resource
    private ThreadService threadService;

    @Test
    public void testAsyncThreadPool() throws InterruptedException {
        System.out.println("主线程开始 name -->" + Thread.currentThread().getName());
        threadService.doTaskA();
        threadService.doTaskB();
        System.out.println("主线程结束 name -->" + Thread.currentThread().getName());
        System.out.println("Hello World");
    }
}
