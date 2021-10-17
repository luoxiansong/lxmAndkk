package com.cqkk.config.mq.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

//@Component
public class KafkaConsumer {
    private static final String MY_TOPIC = "TOPIC_STRING";

    private Log log = LogFactory.getLog(KafkaConsumer.class);

    private static final String MY_TOPIC_LIST = "TOPIC_LIST";

    @Resource
    ConsumerFactory consumerFactory;

    // 消息过滤器
    //实现了一个"过滤奇数、接收偶数"的过滤策略，我们向topic1发送0-99总共100条消息，看一下监听器的消费情况，可以看到监听器只消费了偶数
    @Bean
    public ConcurrentKafkaListenerContainerFactory filterContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        // 被过滤的消息将被丢弃
        factory.setAckDiscarded(true);
        // 消息过滤策略
        factory.setRecordFilterStrategy(consumerRecord -> {
            if (Integer.parseInt(consumerRecord.value().toString()) % 2 != 0) {
                return false;
            }
            //返回true消息则被过滤
            return true;
        });
        return factory;
    }

    //消息监听
    @KafkaListener(id = "listen", topics = {MY_TOPIC})
    public void onMessage(String message) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("开始接受简单消息");
        log.info("接收到的简单消费为：" + message);
    }

//
//    //指定topic、partition、offset消费
//    //@Description 同时监听topic1和topic2，监听topic1的0号分区、topic2的 "0号和1号" 分区，指向1号分区的offset初始值为8
//    @KafkaListener(id = "consumer1", groupId = "felix-group", topicPartitions = {
//            //属性解释：
//            //① id：消费者ID；
//            //② groupId：消费组ID；
//            //③ topics：监听的topic，可监听多个；
//            //④ topicPartitions：可配置更加详细的监听信息，可指定topic、partition、offset监听。
//            //下面onMessage2监听的含义：监听topic1的0号分区，同时监听topic2的0号分区和topic2的1号分区里面offset从8开始的消息。
//            //注意：topics和topicPartitions不能同时使用；
//            @TopicPartition(topic = "topic1", partitions = {"0"}),
//            @TopicPartition(topic = "topic2", partitions = "0",
//                    partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "8"))})
//    public void onMessage2(ConsumerRecord<?, ?> record) {
//        log.info("接收到的简单消费主题为：" + record.topic() + "-分区为：" + record.partition() + "-消息值为：" + record.value());
//    }

    //批量消费,用List接收
    @KafkaListener(id = "consumer2", groupId = "felix-group", topics = {MY_TOPIC_LIST})
    public void onMessage3(List<ConsumerRecord<?, ?>> records) {
        log.info("批量消费一次,records.size()=" + records.size());
        records.forEach(e -> {
            System.out.println(e.key() + "-" + e.value());
        });
    }

    // 将这个异常处理器的BeanName放到@KafkaListener注解的errorHandler属性里面
//    @KafkaListener(topics = {MY_TOPIC}, errorHandler = "consumerAwareErrorHandler")
//    public void onMessage4(ConsumerRecord<?, ?> record) throws Exception {
//        log.info("测试消费异常" + record.value());
//        throw new Exception("简单消费-模拟异常");
//    }

//    @KafkaListener(topics = {"topic1"}, errorHandler = "consumerAwareErrorHandler")
//    public void onMessage5(List<ConsumerRecord> record) throws Exception {
//        log.info("批量消费一次。。" + record.get(0).value());
//        throw new Exception("批量消费-模拟异常");
//    }
//
//    // 消息过滤监听
//    //实现了一个"过滤奇数、接收偶数"的过滤策略，我们向topic1发送0-99总共100条消息，看一下监听器的消费情况，可以看到监听器只消费了偶数
//    @KafkaListener(topics = {"topic1"}, containerFactory = "filterContainerFactory")
//    public void onMessage6(ConsumerRecord<?, ?> record) {
//        log.info("record.value()==>" + record.value());
//    }
//
//    //在实际开发中，我们可能有这样的需求，应用A从TopicA获取到消息，经过处理后转发到TopicB，再由应用B监听处理消息，
//    // 即一个应用处理完成后将该消息转发至其他应用，完成消息的转发。
//    //在SpringBoot集成Kafka实现消息的转发也很简单，只需要通过一个@SendTo注解，被注解方法的return值即转发的消息内容
//    //消息转发
//    @KafkaListener(topics = {"topic1"})
//    @SendTo("topic2")
//    public String onMessage7(ConsumerRecord<?, ?> record) {
//        log.info("我接收到了topic1的消息，我要转发消息到topic2");
//        return record.value() + "forward message";
//    }
}
