package com.cqkk.config.jdk8.Async.Async.ceshiAsync;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.lucene.util.NamedThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 * 自定义线程池；当然可以不用定义线程池，会使用自带的线程池，但是通过线程池控制可以更好的控制并发；我这里用了三种线程池.
 *
 * @author liuyuan
 * @version ThreadPoolTaskConfig.java, v 0.1 2021-11-15 11:10
 */
@Configuration
@EnableAsync
public class ThreadPoolTaskConfig {

    /**
     * 核心线程数（默认线程数）
     */
    private static final int CORE_POOL_SIZE = 10;
    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = 100;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int KEEP_ALIVE_TIME = 10;
    /**
     * 缓冲队列数
     */
    private static final int QUEUE_CAPACITY = 200;
    /**
     * 线程池名前缀
     */
    private static final String THREAD_NAME_PREFIX = "Async-Service-";

    /**
     * bean的名称，默认为首字母小写的方法名
     */
    // spring管理的线程池，顶级父类也是Executor
    @Bean("taskExecutor1")
    public ThreadPoolTaskExecutor taskExecutor1() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);

        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

    /*************************************************分割线*************************************************/

    private static final int THREADS = Runtime.getRuntime().availableProcessors() + 1;
    private final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            // -%d不要少
            .setNameFormat("async-task-name-%d")
            .setDaemon(true)
            .build();

    // JDK原生线程池，建议返回值为ExecutorService，可以调用submit()方法启动线程
    @Bean("taskExecutor2")
    public Executor taskExecutor2() {
        return new ThreadPoolExecutor(THREADS, 2 * THREADS,
                5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024),
                threadFactory, (r, executor) -> {
            // 打印日志,添加监控等
            System.out.println("task is rejected!");
        });
    }

    /*****************************************************分割线**************************************************/

    // NamedThreadFactory需要自己定义
    @Bean("taskExecutor3")
    public Executor taskExecutor3() {
        return new ThreadPoolExecutor(2, 50, 60L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(2048),
                new NamedThreadFactory("api-apply-thread"));
    }
}
