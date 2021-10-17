package com.cqkk.config.mq.rabbitmq.topic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: lxmAndkk
 * @description: TopicTotalReceiver.java
 * @author: luo kk
 * @create: 2021-07-08 22:32
 */
@Component
@RabbitListener(queues = "topic.woman")
public class TopicTotalReceiver {
    Log log = LogFactory.getLog(TopicTotalReceiver.class);

    @RabbitHandler
    public void process(Map testMessage) {
        log.info("TopicTotalReceiver消费者收到消息  : " + testMessage.toString());
    }
}