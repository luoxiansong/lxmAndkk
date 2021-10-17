package com.cqkk.config.mq.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: lxmAndkk
 * @description: 消费者 定时启动、停止监听器
 * @author: luo kk
 * @create: 2021-07-24 20:00
 */
@Component
@EnableScheduling
public class CronTimer {

    private Log log = LogFactory.getLog(CronTimer.class);
    private static final String MY_TOPIC = "TOPIC_STRING";
    /**
     * @KafkaListener注解所标注的方法并不会在IOC容器中被注册为Bean， 而是会被注册在KafkaListenerEndpointRegistry中，
     * 而KafkaListenerEndpointRegistry在SpringIOC中已经被注册为Bean
     **/

    @Resource
    private KafkaListenerEndpointRegistry registry;

    @Resource
    private ConsumerFactory consumerFactory;


    // 监听器容器工厂(设置禁止KafkaListener自启动)
    @Bean
    public ConcurrentKafkaListenerContainerFactory delayContainerFactory() {
        ConcurrentKafkaListenerContainerFactory container = new ConcurrentKafkaListenerContainerFactory();
        container.setConsumerFactory(consumerFactory);
        //禁止KafkaListener自启动
        container.setAutoStartup(false);
        return container;

    }

    // 监听器
    @KafkaListener(id = "timingConsumer", topics = MY_TOPIC,groupId = "felix-group", containerFactory = "delayContainerFactory")
    public void onMessage(List<ConsumerRecord<?, ?>> records) {
        log.info("消费成功：" + records.size());
    }

    // 定时启动监听器
    @Scheduled(cron = "0 40 22 * * ?")
    public void startListener() {
        log.info("启动监听器...");
        // "timingConsumer"是@KafkaListener注解后面设置的监听器ID,标识这个监听器
        if (!registry.getListenerContainer("timingConsumer").isRunning()) {
            registry.getListenerContainer("timingConsumer").start();
        }
        //registry.getListenerContainer("timingConsumer").resume();
    }

    //  定时停止监听器
    @Scheduled(cron = "0 42 22 * * ?")
    public void shutDownListener() {
        log.info("关闭监听器...");
        registry.getListenerContainer("timingConsumer").pause();
    }

}