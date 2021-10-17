package com.cqkk.config.mq.rabbitmq;

import com.baomidou.mybatisplus.extension.api.R;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mapstruct.Qualifier;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: lxmAndkk
 * @description: 配置消息确认回调函数
 * @author: luo kk
 * @create: 2021-07-09 06:47
 */
//先从总体的情况分析，推送消息存在四种情况：
//①消息推送到server，但是在server里找不到交换机
//②消息推送到server，找到交换机了，但是没找到队列
//③消息推送到sever，交换机和队列啥都没找到
//④消息推送成功
@Configuration
public class RabbitConfig {
    Log log = LogFactory.getLog(RabbitConfig.class);

    @Bean
    public RabbitTemplate createRabbitTemplate(CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(cachingConnectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("ConfirmCallback:     " + "相关数据：" + correlationData);
                log.info("ConfirmCallback:     " + "确认情况：" + ack);
                log.info("ConfirmCallback:     " + "原因：" + cause);
            }
        });

        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                log.info("ReturnCallback:     " + "消息：" + returnedMessage.getMessage());
                log.info("ReturnCallback:     " + "回应码：" + returnedMessage.getReplyCode());
                log.info("ReturnCallback:     " + "回应信息：" + returnedMessage.getReplyText());
                log.info("ReturnCallback:     " + "交换机：" + returnedMessage.getExchange());
                log.info("ReturnCallback:     " + "路由键：" + returnedMessage.getRoutingKey());
            }
        });
        return rabbitTemplate;
    }

    @Bean
    public CachingConnectionFactory createConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("127.0.0.1", 5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        connectionFactory.setPublisherReturns(true);
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);

        return connectionFactory;
    }
}