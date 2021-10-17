package com.cqkk.config.mq.rabbitmq.fanout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: lxmAndkk
 * @description: 消息消费类B
 * @author: luo kk
 * @create: 2021-07-09 06:40
 */
@RabbitListener(queues = "fanout.B")
@Component
public class FanoutReceiverB {

    private Log log = LogFactory.getLog(FanoutReceiverB.class);

    @RabbitHandler
    public void process(Map testMessage) {
        log.info(("FanoutReceiverB消费者收到消息  : " + testMessage.toString()));
    }
}