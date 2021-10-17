package com.cqkk.config.mq.rabbitmq.topic;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @program: lxmAndkk
 * @description: 主题交换机
 * @author: luo kk
 * @create: 2021-07-08 22:11
 */
@Component
@Configuration
public class TopicRabbitConfig {

    //绑定键
    public static final String man = "topic.man";
    public static final String woman = "topic.woman";

    @Bean
    public Queue manQueue() {
        return new Queue(TopicRabbitConfig.man);
    }

    @Bean
    public Queue womanQueue() {
        return new Queue(TopicRabbitConfig.woman);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    //将firstQueue和topicExchange绑定,而且绑定的键值为topic.man
    //这样只要是消息携带的路由键是topic.man,才会分发到该队列
    @Bean
    public Binding bindingExchangeMessage() {
        return BindingBuilder.bind(manQueue()).to(topicExchange()).with(man);
    }

    //将secondQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
    // 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
    @Bean
    public Binding bingingExchangeMessage2() {
        return BindingBuilder.bind(womanQueue()).to(topicExchange()).with("topic.#");
    }
}