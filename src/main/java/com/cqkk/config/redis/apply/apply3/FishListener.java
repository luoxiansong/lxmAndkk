package com.cqkk.config.redis.apply.apply3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/*编写监听类来监听消息*/
//Cat监听
@Component
public class FishListener implements MessageListener {

    private Log log = LogFactory.getLog(FishListener.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("我是fish监听" + message.toString());
    }
}
