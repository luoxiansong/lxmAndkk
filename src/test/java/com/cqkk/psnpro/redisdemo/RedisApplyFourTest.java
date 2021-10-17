package com.cqkk.psnpro.redisdemo;

import com.cqkk.config.redis.apply.apply4.Customer;
import com.cqkk.config.redis.apply.apply4.Producer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @program: lxmAndkk
 * @description: ceshi Four
 * @author: luo kk
 * @create: 2021-07-07 07:09
 */
@SpringBootTest
public class RedisApplyFourTest {

    @Autowired
    private Producer producer;

    @Autowired
    private Customer customer;

    @Test
    public void test1() {
        while (true) {
            producer.producer();
            customer.customer();
        }

    }
}