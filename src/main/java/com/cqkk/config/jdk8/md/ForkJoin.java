package com.cqkk.config.jdk8.md;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * 要想使用Fark—Join，类必须继承
 * RecursiveAction（无返回值）Or RecursiveTask（有返回值）
 */
@Component
public class ForkJoin extends RecursiveTask<Long> {

    private Log log = LogFactory.getLog(ForkJoin.class);

    private Long start;
    private Long end;

    // 定义阙值
    private static final Long THRESHOLD = 10000L;

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (long i = start; i < end; i++) {
                sum += i;
            }
            return sum;
        } else {
            long mibble = (end - start) / 2;
            ForkJoin left = new ForkJoin();
            left.setStart(start);
            left.setEnd(mibble);
            //拆分子任务，压入线程队列
            left.fork();
            ForkJoin right = new ForkJoin();
            right.setEnd(end);
            right.setStart(mibble);
            right.fork();
            //合并并返回
            return left.join() + right.join();
        }

    }

    //实现数的累加
    public void test1() {
        //开始时间
        Instant start = Instant.now();

        //这里需要一个线程池的支持
        ForkJoinPool pool = new ForkJoinPool();

        ForkJoin forkJoin = new ForkJoin();

        forkJoin.setStart(0L);
        forkJoin.setEnd(10000L);
        // 没有返回值     pool.execute();
        // 有返回值
        Long sum = pool.invoke(forkJoin);

        //结束时间
        Instant end = Instant.now();
        System.out.println((Duration.between(start, end).getSeconds()));
    }

    //java8 并行流 parallel()
    public void test7() {
        //开始时间
        Instant start = Instant.now();

        //并行流计算
        LongStream.rangeClosed(0, 10000000000L).parallel().reduce(0, Long::sum);

        //结束时间
        Instant end = Instant.now();
        System.out.println((Duration.between(start, end).getSeconds()));
    }

    public void test8() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        list.stream().forEach(System.out::print);

        list.parallelStream()
                .forEach(System.out::print);
    }


}
