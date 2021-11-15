package com.cqkk.controller;

import com.cqkk.config.jdk8.Async.Async.ceshiAsync.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@RestController
@RequestMapping(value = "/async")
public class AsyncThreadController {

    @Autowired
    private ThreadService threadService;

    @RequestMapping("/async")
    public void testAsync() throws Exception {
        System.out.println("主线程开始 name -->" + Thread.currentThread().getName());
        threadService.doTaskA();
        threadService.doTaskB();
        System.out.println("主线程结束 name -->" + Thread.currentThread().getName());
        System.out.println("Hello World");
    }
    //主线程开始 name -->http-nio-8889-exec-2
    //TaskA thread name->Async-Service-1
    //主线程结束 name -->http-nio-8889-exec-2
    //Hello World
    //TaskB thread name->async-task-name-0
    //TaskA 耗时：2012
    //TaskB耗时：2012

    //结果：主线程、TaskA和TaskB线程名都不相同，说明启用了不同的线程；


    //使用Future，返回异步的结果
    @GetMapping("/async1")
    public void testAsync1() throws Exception {
        System.out.println("主线程开始 name -->" + Thread.currentThread().getName());

        Future<Map<String, String>> future = threadService.doTaskC();
        while (!future.isDone()) {
            System.out.println("Wait asyncTaskWithResult.");
            Thread.sleep(1000);
        }
        System.out.println("asyncTaskWithResult result is:" + future.get().toString());

        System.out.println("主线程结束 name -->" + Thread.currentThread().getName());
        System.out.println("Hello World");
    }
    //主线程开始 name -->http-nio-8889-exec-2
    //Wait asyncTaskWithResult.
    //TaskC thread name->api-apply-thread-1-thread-1
    //Wait asyncTaskWithResult.
    //Wait asyncTaskWithResult.
    //TaskC耗时：2010
    //asyncTaskWithResult result is:{return=返回值是一个map！}
    //主线程结束 name -->http-nio-8889-exec-2

    //使用FutureTask，返回异步的结果
    //采用普通的方式，开启线程；
    @GetMapping("/async2")
    public void testAsync2() throws Exception {
        System.out.println("主线程开始 name -->" + Thread.currentThread().getName());

        FutureTask<String> future = threadService.doTaskD();
        boolean flag = true;
        while (flag) {
            //异步任务完成并且未被取消，则获取返回的结果
            if (future.isDone() && !future.isCancelled()) {
                System.out.println("asyncTaskWithResult result is:" + future.get());
                flag = false;
            }
        }

        System.out.println("主线程结束 name -->" + Thread.currentThread().getName());
        System.out.println("Hello World");
    }
    //主线程开始 name -->http-nio-8889-exec-1
    //TaskC耗时：2001
    //asyncTaskWithResult result is:成功获取future异步任务结果,threadName = api-apply-thread-1-thread-1
    //主线程结束 name -->http-nio-8889-exec-1
    //Hello World

    //CompletableFuture 进行变换测试
    @RequestMapping(value = "async3")
    public String testAsync3() throws Exception {
        System.out.println("主线程开始 name -->" + Thread.currentThread().getName());
        threadService.doTaskG();
        System.out.println("主线程结束 name -->" + Thread.currentThread().getName());
        return "Hello World";
    }
    //主线程开始 name -->http-nio-8889-exec-2
    //线程1Async-Service-1
    //线程2async-task-name-0
    //return: hellodosomething
    //主线程结束 name -->http-nio-8889-exec-2
    //分析：可以看到，我使用的是异步方法，并且结果也开启了了两个线程，但是获取返回值还是阻塞了主线程的执行；

    //CompletableFuture 进行消耗测试
    @RequestMapping(value = "async4")
    public String testAsync4() throws Exception {
        System.out.println("主线程开始 name -->" + Thread.currentThread().getName());
        threadService.doTaskH();
        System.out.println("主线程结束 name -->" + Thread.currentThread().getName());
        return "Hello World";
    }
    //主线程开始 name -->http-nio-8889-exec-4
    //线程1Async-Service-1
    //主线程结束 name -->http-nio-8889-exec-4
    //线程2async-task-name-0
    //return: CompletableFuture===thenAcceptAsync
    //分析：执行supplyAsync()和thenAcceptAsync()时分别开了不同的线程池，并且返回值没有阻塞主线程，
    //也就是说实现了异步处理的结果并且将结果交给另外一个异步事件处理线程来处理。
    //注意：在函数结尾千万不要加.join()方法，否则还是会阻塞处理返回值；


}
