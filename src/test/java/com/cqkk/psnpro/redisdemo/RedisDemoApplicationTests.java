package com.cqkk.psnpro.redisdemo;

import com.cqkk.config.redis.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;

@SpringBootTest
public class RedisDemoApplicationTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemp;

    @Autowired
    private RedisUtil redisUtil;

    //操作String字符串类型数据时，使用RedisTemplate类中的opsForValue方法操作。
    //操作List列表类型数据时，使用RedisTemplate类中的opsForList方法操作。
    //操作Set集合类型数据时，使用RedisTemplate类中的opsForSet方法操作。
    //操作Hash哈希类型数据时，使用RedisTemplate类中的opsForHash方法操作。
    //操作Zset有序集合类型数据时，使用RedisTemplate类中的opsForZSet方法操作。
    @Test
    public void test1() {
        //向redis添加数据
        redisTemplate.opsForValue().set("keys", "value值");
        System.out.println((redisTemplate.opsForValue().get("keys")));
    }

    //===================String===========================
    //普通缓存放入
    @Test
    public void test2() {
        System.out.println(redisUtil.set("lxm", "169"));
    }

    //普通缓存获取
    @Test
    public void test3() {
        System.out.println(redisUtil.get("lxm"));
    }

    //普通缓存放入并设置时间
    @Test
    public void test4() {
        System.out.println(redisUtil.set("lxm", 3, 340));
    }

    //递增
    @Test
    public void test5() {
        System.out.println(redisUtil.incr("lxm", 3));
    }

    //递减
    @Test
    public void test6() {
        System.out.println(redisUtil.decr("lxm", 3));
    }

    //===================Hash===========================
    //HashGet
    @Test
    public void test7() {
        System.out.println(redisUtil.hget("lkk", "high"));
    }

    //获取hashKey对应的所有键值
    @Test
    public void test8() {
        System.out.println(redisUtil.hmget("lkk"));
    }

    //HashSet
    @Test
    public void test9() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("high", 157);
        map.put("sex", "男");
        System.out.println(redisUtil.hmset("lkk", map));
    }

    //HashSet 并设置时间
    @Test
    public void test10() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("high", 157);
        map.put("sex", "男");
        System.out.println(redisUtil.hmset("lkk", map, 60));
    }

    //向一张hash表中放入数据,如果不存在将创建
    @Test
    public void test11() {
        System.out.println(redisUtil.hset("lkk", "sex", "男"));
    }

    //向一张hash表中放入数据,如果不存在将创建
    @Test
    public void test12() {
        System.out.println(redisUtil.hset("lkk", "sex", "男", 300));
    }

    //删除hash表中的值
    @Test
    public void test13() {
        redisUtil.hdel("lkk", "high", "sex");
    }

    //判断hash表中是否有该项的值
    @Test
    public void test14() {
        System.out.println(redisUtil.hHasKey("lkk", "sex"));
    }

    //hash递增 如果不存在,就会创建一个 并把新增后的值返回
    @Test
    public void test15() {
        System.out.println(redisUtil.hincr("lkk", "high", 3.0));
    }

    //hash递减
    @Test
    public void test16() {
        System.out.println(redisUtil.hdecr("lkk", "high", 3.0));
    }

    //===================Hash===========================
    //根据key获取Set中的所有值
    @Test
    public void test17() {
        System.out.println(redisUtil.sGet("xm"));
    }

    //根据value从一个set中查询,是否存在
    @Test
    public void test18() {
        System.out.println(redisUtil.sHasKey("xm", "kak"));
    }

    //将数据放入set缓存
    @Test
    public void test19() {
        System.out.println(redisUtil.sSet("xm", "1", "2", "kak"));
    }

    //将set数据放入缓存
    @Test
    public void test20() {
        System.out.println(redisUtil.sSetAndTime("luo", 30, "3", "5", "6"));
    }

    //获取set缓存的长度
    @Test
    public void test21() {
        System.out.println(redisUtil.sGetSetSize("xm"));
    }

    //移除值为value的
    @Test
    public void test22() {
        System.out.println(redisUtil.setRemove("xm", "2"));
    }

    //===================list===========================
    //获取list缓存的内容
    @Test
    public void test23() {
        System.out.println(redisUtil.lGet("kk", 0, 3));
    }

    //获取list缓存的长度
    @Test
    public void test24() {
        System.out.println(redisUtil.lGetListSize("kk"));
    }

    //通过索引 获取list中的值
    @Test
    public void test25() {
        System.out.println(redisUtil.lGetIndex("kk", 0));
    }

    //将list放入缓存
    @Test
    public void test26() {
        System.out.println(redisUtil.lSet("kk", "18"));
    }

    //将list放入缓存
    @Test
    public void test27() {
        System.out.println(redisUtil.lSet("kk", "18", 30));
    }

    //将list放入缓存
    @Test
    public void test28() {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("18");
        objects.add("sex");
        System.out.println(redisUtil.lSetList("lxs", objects));
    }

    //将list放入缓存
    @Test
    public void test29() {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("18");
        objects.add("sex");
        System.out.println(redisUtil.lSetList("lxs", objects, 30));
    }

    //根据索引修改list中的某条数据
    @Test
    public void test30() {
        System.out.println(redisUtil.lUpdateIndex("lxs", 0, "newValue"));
    }

    //移除N个值为value
    @Test
    public void test31() {
        System.out.println(redisUtil.lRemove("lxs", 1, "newValue"));
    }
}
