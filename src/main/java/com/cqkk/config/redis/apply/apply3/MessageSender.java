package com.cqkk.config.redis.apply.apply3;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@EnableScheduling
@Component
public class MessageSender {

    @Resource
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0 0/30 * * 6 *")
    public void sendCatMessage(){
        redisTemplate.convertAndSend("cat","猫");
    }

    @Scheduled(cron = "0 0/15 * * 6 *")
    public void sendFishMessage(){
        redisTemplate.convertAndSend("fish","鱼");
    }
}
