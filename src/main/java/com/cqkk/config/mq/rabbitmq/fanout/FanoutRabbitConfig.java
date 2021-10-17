package com.cqkk.config.mq.rabbitmq.fanout;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @program: lxmAndkk
 * @description: Fanout Exchang 扇型交换机
 * @author: luo kk
 * @create: 2021-07-09 06:29
 */
@Configuration
public class FanoutRabbitConfig {

    /**
     * 创建三个队列 ：fanout.A   fanout.B  fanout.C
     * 将三个队列都绑定在交换机 fanoutExchange 上
     * 因为是扇型交换机, 路由键无需配置,配置也不起作用
     */
    @Bean
    public Queue queueA() {
        return new Queue("fanout.A");
    }

    @Bean
    public Queue queueB() {
        return new Queue("fanout.B");
    }

    @Bean
    public Queue queueC() {
        return new Queue("fanout.C");
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    @Bean
    public Binding bindingExchangeA() {
        return BindingBuilder.bind(queueA()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingExchangeB() {
        return BindingBuilder.bind(queueB()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingExchangeC() {
        return BindingBuilder.bind(queueC()).to(fanoutExchange());
    }
}