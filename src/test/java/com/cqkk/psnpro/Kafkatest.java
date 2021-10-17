package com.cqkk.psnpro;

import com.cqkk.config.mq.kafka.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class Kafkatest {

    @Resource
    KafkaProducer kafkaProducer;

    @Test
    public void test1() {
        kafkaProducer.sendMessage();
    }

   /* //测试kafka事务提交
    @Test
    public void test2() throws InterruptedException {
        kafkaProducer.sendMessageTransaction();
    }

    //测试消息过滤器
    @Test
    public void test3() {
        kafkaProducer.sendMessage1();
    }*/
}
