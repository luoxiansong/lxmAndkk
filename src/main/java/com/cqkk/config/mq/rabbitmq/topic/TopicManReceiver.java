package com.cqkk.config.mq.rabbitmq.topic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: lxmAndkk
 * @description: topic.man
 * @author: luo kk
 * @create: 2021-07-08 22:29
 */
@Component
@RabbitListener(queues = "topic.man")
public class TopicManReceiver {
    Log log = LogFactory.getLog(TopicManReceiver.class);

    @RabbitHandler
    public void process(Map testMessage) {
       log.info("TopicManReceiver消费者收到消息  : " + testMessage.toString());
    }
}