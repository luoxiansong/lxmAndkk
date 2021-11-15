package com.cqkk.config.jdk8.Async.Async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class AsyncTest {

    @Async  //标注使用
    public void asyncMethodWithVoidReturnType() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Execute method asynchronously. "
                + Thread.currentThread().getName());
    }
    //使用的方式非常简单，一个标注即可解决所有的问题。

    @Async
    public Future<String> asyncMethodWithReturnType() {
        System.out.println("Execute method asynchronously - "
                + Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
            return new AsyncResult<>("hello world !!!!");
        } catch (InterruptedException e) {
            //
        }
        return null;
    }
    //以上示例可以发现，返回的数据类型为Future类型，其为一个接口。具体的结果类型为AsyncResult,这个是需要注意的地方。

}
