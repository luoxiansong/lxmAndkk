package com.cqkk.psnpro.redisdemo;

import com.cqkk.config.redis.apply.RedisApplyOne;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedisApplyOneTest {

    @Resource
    private RedisApplyOne redisApplyone;

    @Test
    public void test1() {
        for (int i = 0; i < 10; i++) {
            redisApplyone.doSendMail("kk");
            System.out.println(("我是第" + i + "个"));
        }
    }
}
