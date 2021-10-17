package com.cqkk.config.mq.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class KafkaProducer {

    private Log log = LogFactory.getLog(KafkaProducer.class);

    private static final String MY_TOPIC = "TOPIC_STRING";

    private static final String MY_TOPIC_LIST = "TOPIC_LIST";

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Scheduled(cron = "0 0/2 * * * *")
    public void sendMessage() {
        log.info("=======开始简单推送消息=========");
        kafkaTemplate.send(MY_TOPIC, "12322323");
        log.info("=======推送简单消息完毕=========");
    }

    // 消费者接收的改成String类型 public void onMessage1(String record) 要是想消费者接收的有topic等类型的话，
    // 生产者得ConsumerRecord<?, ?> record=new ConsumerRecord("topic1",1,1,2,normalMessage);
    // kafkaTemplate.send("topic1", record.toString());

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendMessage1() {
        log.info("=======开始推送MY_TOPIC_LIST消息=========");
        for (int i = 0; i < 10; i++) {
            ProducerRecord record = new ProducerRecord(MY_TOPIC_LIST, "key" + i, String.valueOf(i));
            kafkaTemplate.send(record);
        }


        //   ProducerRecord record = new ProducerRecord(MY_TOPIC, "key"+System.currentTimeMillis(), "简单消息,请查收");
        //        kafkaTemplate.send(record);

        kafkaTemplate.send(MY_TOPIC, "2");
        log.info("=======推送消息完毕=========");
    }



   /* //带回调的生产者1
    @Scheduled(cron = "0 0/30 * * * * ")
    public void sendMessageCallback() {
        log.info("=======带回调的生产者推送消息=======");
        kafkaTemplate.send(MY_TOPIC, "data", "带回调的生产者推送的消息").addCallback(success -> {
            // 消息发送到的topic
            String topic = success != null ? success.getProducerRecord().topic() : null;
            // 消息发送到的分区
            Integer partition = success.getProducerRecord().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            log.info("发送消息成功:" + topic + "-" + partition + "-" + offset);
        }, failure -> {
            log.error("失败的原因为：" + failure.getMessage());
        });
    }

    //带回调的生产者1
    @Scheduled(cron = "0 0/15 * * * * ")
    public void sendMessageCallback1() {
        kafkaTemplate.send(MY_TOPIC, "data", "带回调的生产者推送的消息1").addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("带回调的生产者推送的消息1发送消息失败" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("带回调的生产者推送的消息1发送成功" + result.getRecordMetadata().topic() + "-"
                        + result.getRecordMetadata().partition() + "-"
                        + result.getRecordMetadata().offset());
            }
        });
    }

    //事务提交
    @Scheduled(cron = "0 0/20 * * * * ")
    public void sendMessageTransaction() throws InterruptedException {
        // 声明事务：后面报错消息不会发出去
        kafkaTemplate.executeInTransaction(kafkaOperations -> {
            kafkaOperations.send(MY_TOPIC, "data", "test executeInTransaction");
            throw new RuntimeException("fail");
        });

        // 不声明事务：后面报错但前面消息已经发送成功了
        kafkaTemplate.send(MY_TOPIC, "data", "test executeInTransaction");
        throw new RuntimeException("fail");
    }

    //实现了一个"过滤奇数、接收偶数"的过滤策略，我们向topic1发送0-99总共100条消息，看一下监听器的消费情况，可以看到监听器只消费了偶数
    public void sendMessage1() {
        log.info("=======开始推送消息=========");
        for (int i = 0; i < 99; i++) {
            kafkaTemplate.send(MY_TOPIC, "data", String.valueOf(i));
        }
        log.info("=======推送消息完毕=========");
    }*/
}
