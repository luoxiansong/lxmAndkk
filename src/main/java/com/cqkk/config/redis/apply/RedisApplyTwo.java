package com.cqkk.config.redis.apply;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Component
public class RedisApplyTwo {

    private Log log = LogFactory.getLog(RedisApplyTwo.class);

    @Resource
    public RedisTemplate redisTemplate;

    public static final String SCORE_RANK = "score_rank";//key声明为score_rank

    /*批量新增*/
    public void batchAdd() {
        HashSet<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        long start = System.currentTimeMillis();//开始时间
        log.info("开始时间为:" + start);
        for (int i = 0; i < 10000; i++) {
            DefaultTypedTuple<String> tuple = new DefaultTypedTuple("小茗" + i, 1D + i);
            tuples.add(tuple);
        }
        log.info("循环时间:" + (System.currentTimeMillis() - start));
        Long num = redisTemplate.opsForZSet().add(SCORE_RANK, tuples);
        log.info("批量新增时间为:" + (System.currentTimeMillis() - start));
        log.info("受影响的行数为:" + num);
    }

    /*获取排行榜列表*/
    //获取前十名
    public void getList() {
        Set<String> set = redisTemplate.opsForZSet().reverseRange(SCORE_RANK, 0, 9);
        log.info("获取到的排行列表为:" + JSONObject.toJSONString(set));
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores(SCORE_RANK, 0, 9);
        log.info("获取到的排行和分数列表:" + JSON.toJSONString(tuples));
    }

    /*新增李四的分数*/
    public void add() {
        Boolean aBoolean = redisTemplate.opsForZSet().add(SCORE_RANK, "李四", 8899);
        if (aBoolean) log.info("新增成功");
    }

    /*获取李四单人的排行*/
    public void find() {
        Long rank = redisTemplate.opsForZSet().reverseRank(SCORE_RANK, "李四");
        log.info("李四的排名为：" + rank);
        Double score = redisTemplate.opsForZSet().score(SCORE_RANK, "李四");
        log.info("李四的分数为：" + score);
    }

    /*统计分数之间有多少人*/
    public void count() {
        Long count = redisTemplate.opsForZSet().count(SCORE_RANK, 8001, 9000);
        log.info("统计8001-9000之间的人数:" + count);
    }

    /*获取集合的基数(数量大小)*/
    public void zCard() {
        Long zCard = redisTemplate.opsForZSet().zCard(SCORE_RANK);
        log.info("集合的基数为：" + zCard);
    }

    /*使用加法操作分数*/
    public void  incrementScore(){
        Double score = redisTemplate.opsForZSet().incrementScore(SCORE_RANK, "李四", 100);
        log.info("李四分数+1000后：" + score);
    }
}
