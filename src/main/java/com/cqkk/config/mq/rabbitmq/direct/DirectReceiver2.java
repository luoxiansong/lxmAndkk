package com.cqkk.config.mq.rabbitmq.direct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: lxmAndkk
 * @description: 消息接收监听类2
 * @author: luo kk
 * @create: 2021-07-08 18:42
 */

//那么直连交换机既然是一对一，那如果咱们配置多台监听绑定到同一个直连交互的同一个队列，会怎么样？
//可以看到是实现了轮询的方式对消息进行消费，而且不存在重复消费。
@Component
@RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
public class DirectReceiver2 {

    private Log log = LogFactory.getLog(DirectReceiver2.class);

    @RabbitHandler
    public void process(Map testMessage) {
        log.info("DirectReceiver2消费者接收到的消息为:" + testMessage.toString());
    }
}