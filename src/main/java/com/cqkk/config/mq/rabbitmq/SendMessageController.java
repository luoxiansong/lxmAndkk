package com.cqkk.config.mq.rabbitmq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//简单的接口进行消息推送
@RestController
@RequestMapping("/sendMessage")
public class SendMessageController {
    private Log log = LogFactory.getLog(SendMessageController.class);

    //使用RabbitTemplate,这提供了接收/发送等等方法
    @Resource
    public RabbitTemplate rabbitTemplate;

    @RequestMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test,message hello";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        HashMap<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        log.info("sendDirectMessage发送消息成功");
        return "ok";
    }

    @RequestMapping("/sendTopicMessage1")
    public String sendTopicMessage1() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: M A N";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        HashMap<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("topicExchange", "topic.man", map);
        log.info("sendTopicMessage1发送消息成功");
        return "ok";
    }

    @RequestMapping("/sendTopicMessage2")
    public String sendTopicMessage2() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: M A N";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        HashMap<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("topicExchange", "topic.woman", map);
        log.info("sendTopicMessage2发送消息成功");
        return "ok";
    }

    @RequestMapping("/sendFanoutMessage")
    public String sendFanoutMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: testFanoutMessage";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        HashMap<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("fanoutExchange", "", map);
        log.info("sendFanoutMessage发送消息成功");
        return "ok";
    }

    //消息推送到server，但是在server里找不到交换机
    @RequestMapping("/TestMessageAck")
    public String TestMessageAck() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: non-existent-exchange test message ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("non-existent-exchange", "TestDirectRouting", map);
        return "ok";
    }

    //消息推送到server，找到交换机了，但是没找到队列
    // ②消息推送到server，找到交换机了，但是没找到队列
    // 这种情况就是需要新增一个交换机，但是不给这个交换机绑定队列，
    // 我来简单地在DirectRabitConfig里面新增一个直连交换机，名叫‘lonelyDirectExchange’，但没给它做任何绑定配置操作：
    @GetMapping("/TestMessageAck2")
    public String TestMessageAck2() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: lonelyDirectExchange test message ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("lonelyDirectExchange", "TestDirectRouting", map);
        return "ok";
    }

}
