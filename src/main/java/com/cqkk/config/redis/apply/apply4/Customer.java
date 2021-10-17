package com.cqkk.config.redis.apply.apply4;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @program: lxmAndkk
 * @description:
 * @author: luo kk
 * @create: 2021-07-07 07:17
 */
@Component
@EnableScheduling
public class Customer {
    public static final String REDIS_KEY = "My_KEY";
    public static final String OUT_REDIS_KEY = "My_KEY_Out";
    public Log log = LogFactory.getLog(Customer.class);
    @Resource
    public RedisTemplate redisTemplate;

    @Scheduled(cron = "0 0/4 * * 5 *")
    public void customer() {
        String value = (String) redisTemplate.opsForList().rightPop(REDIS_KEY, 2, TimeUnit.SECONDS);
        log.info("REDIS_KEY取出了value" + value);
        Long size = redisTemplate.opsForList().rightPush(OUT_REDIS_KEY, value);
        log.info("OUT_REDIS_KEY存入了" + value + "，总共" + size + "个");
    }
}