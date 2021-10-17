package com.cqkk.config.mq.rabbitmq;

import com.rabbitmq.client.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyAckReceiver implements ChannelAwareMessageListener {

    Log log = LogFactory.getLog(MyAckReceiver.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //因为传递消息的时候用的map传递,所以将Map从Message内取出需要做些处理
            String msg = message.toString();
            String[] msgArray = msg.split("'");//可以点进Message里面看源码,单引号直接的数据就是我们的map消息数据
            Map<String, String> map = mapStringToMap(msgArray[1].trim(), 3);
            String messageId = map.get("messageId");
            String messageData = map.get("messageData");
            String createTime = map.get("createTime");

            if ("TestDirectQueue".equals(message.getMessageProperties().getConsumerQueue())) {
                log.info("消费的消息来自的队列名为：" + message.getMessageProperties().getConsumerQueue());
                log.info("消息成功消费到  messageId:" + messageId + "  messageData:" + messageData + "  createTime:" + createTime);
                log.info("执行TestDirectQueue中的消息的业务处理流程......");

            }

            if ("fanout.A".equals(message.getMessageProperties().getConsumerQueue())) {
                log.info("消费的消息来自的队列名为：" + message.getMessageProperties().getConsumerQueue());
                log.info("消息成功消费到  messageId:" + messageId + "  messageData:" + messageData + "  createTime:" + createTime);
                log.info("执行fanout.A中的消息的业务处理流程......");

            }

            //第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            channel.basicAck(deliveryTag, true);
            //channel.basicReject(deliveryTag, true);//第二个参数，true会重新放回队列，所以需要自己根据业务逻辑判断什么时候使用拒绝
        } catch (IOException e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }

    //{key=value,key=value,key=value} 格式转换成map
    public Map mapStringToMap(String mapString, int entryNum) {
        HashMap<String, Object> map = new HashMap<>();
        String newString = mapString.substring(1, mapString.length() - 1);
        String[] strings = newString.split(",");
        for (int i = 0; i < strings.length; i++) {
            String[] strings1 = strings[i].split("=");
            if (strings.length - 1 == i) {
                String[] strings2 = strings[i - 1].split("=");
                map.put(strings2[0].trim(), map.get(strings2[0].trim()) + strings1[0]);
            } else {
                map.put(strings1[0].trim(), strings1[1].trim());
            }
        }
        return map;
    }
}
