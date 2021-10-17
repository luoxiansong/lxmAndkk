package com.cqkk.psnpro.redisdemo;

import com.cqkk.config.redis.apply.RedisApplyTwo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedisApplyTwoTest {

    @Resource
    public RedisApplyTwo redisApplytwo;

    @Test
    public void test1() {
        //测试批量新增
        redisApplytwo.batchAdd();
    }

    @Test
    public void test2() {
        //测试获取排行榜前十名
        redisApplytwo.getList();
    }

    @Test
    public void test3() {
        //新增李四的分数
        redisApplytwo.add();
    }

    @Test
    public void test4() {
        //统计分数之间有多少人
        redisApplytwo.count();
    }

    @Test
    public void test5() {
        //获取集合的基数(数量大小)
        redisApplytwo.zCard();
    }

    @Test
    public void test6() {
        //新增李四的分数
        redisApplytwo.find();
    }

    @Test
    public void test7() {
        //使用加法操作分数
        redisApplytwo.incrementScore();
    }
}
