package com.cqkk.config.mq.rabbitmq.direct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: lxmAndkk
 * @description: 消息接收监听类
 * @author: luo kk
 * @create: 2021-07-08 18:42
 */
@Component
@RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
public class DirectReceiver {

    private Log log = LogFactory.getLog(DirectReceiver.class);

    @RabbitHandler
    public void process(Map testMessage) {
        log.info("DirectReceiver消费者接收到的消息为:" + testMessage.toString());
    }
}