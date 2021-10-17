package com.cqkk.config.mq.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;

@Configuration
public class KafkaInitialConfiguration {

    private Log log = LogFactory.getLog(KafkaInitialConfiguration.class);
    // 在执行代码kafkaTemplate.send("topic1", normalMessage)发送消息时，kafka会帮我们自动完成topic的创建工作，
    // 但这种情况下创建的topic默认只有一个分区，分区也没有副本。所以，我们可以在项目中新建一个配置类专门用来初始化topic，如下，

    // 创建一个名为testTopic的Topic并设置分区数为8，分区副本数为2
    @Bean
    public NewTopic initialTopic() {
        return new NewTopic("testTopic", 8, (short) 2);
    }

    // 如果要修改分区数，只需修改配置值重启项目即可
    // 修改分区数并不会导致数据的丢失，但是分区数只能增大不能减小
    @Bean
    public NewTopic updateTopic() {
        return new NewTopic("testTopic", 10, (short) 2);
    }

    //ConsumerAwareListenerErrorHandler 异常处理器
    //通过异常处理器，我们可以处理consumer在消费时发生的异常。
    //新建一个 ConsumerAwareListenerErrorHandler 类型的异常处理方法，用@Bean注入，BeanName默认就是方法名，
    //然后我们将这个异常处理器的BeanName放到@KafkaListener注解的errorHandler属性里面，当监听抛出异常的时候，则会自动调用异常处理器，
    // 新建一个异常处理器，用@Bean注入
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler() {
        return (message, exception, consumer) -> {
            System.out.println("消费异常：" + message.getPayload());
            return null;
        };
    }
}
