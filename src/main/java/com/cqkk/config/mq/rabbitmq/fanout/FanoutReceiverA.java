package com.cqkk.config.mq.rabbitmq.fanout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: lxmAndkk
 * @description: 消息消费类A
 * @author: luo kk
 * @create: 2021-07-09 06:40
 */
@RabbitListener(queues = "fanout.A")
@Component
public class FanoutReceiverA {

    private Log log = LogFactory.getLog(FanoutReceiverA.class);

    @RabbitHandler
    public void process(Map testMessage) {
        log.info(("FanoutReceiverA消费者收到消息  : " + testMessage.toString()));
    }
}