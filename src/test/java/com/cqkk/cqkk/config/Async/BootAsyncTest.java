package com.cqkk.cqkk.config.Async;

import com.cqkk.config.jdk8.Async.Async.AsyncTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@SpringBootTest
public class BootAsyncTest {

    @Resource
    private AsyncTest asyncTest;

    @Test
    public void asyncTest() throws InterruptedException {
        asyncTest.asyncMethodWithVoidReturnType();
        System.out.println("Async");
    }

    @Test
    public void asyncTest2() throws InterruptedException, ExecutionException {
        Future<String> a = asyncTest.asyncMethodWithReturnType();
        System.out.println("Async");
        while (true) {
            if (a.isDone()) {
                System.out.println(a.get());
                break;
            }
            Thread.sleep(2000);
        }
        System.out.println("over");
    }

}
