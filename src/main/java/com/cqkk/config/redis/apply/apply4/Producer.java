package com.cqkk.config.redis.apply.apply4;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: lxmAndkk
 * @description:
 * @author: luo kk
 * @create: 2021-07-07 07:16
 */
@Component
@EnableScheduling
public class Producer {
    public static final String REDIS_KEY = "My_KEY";
    public Log log = LogFactory.getLog(Customer.class);
    @Resource
    public RedisTemplate redisTemplate;

    @Scheduled(cron = "0 0/5 * * 5 *")
    public void producer() {
        for (int i = 0; i < 2; i++) {
            Long size = redisTemplate.opsForList().leftPush(REDIS_KEY, "lkk" + i);
            log.info("REDIS_KEY现在存在" + size + "个");
        }
    }
}