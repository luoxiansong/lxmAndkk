package com.cqkk.config.redis.apply.apply5;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.UUID;

/**
 * @program: lxmAndkk
 * @description: Service
 * @author: luo kk
 * @create: 2021-07-08 06:41
 */
@Service
public class Apply5Service {

    private Log log = LogFactory.getLog(Apply5Service.class);

    @Resource
    private RedisLock redisLock;

    //商品详情
    private static HashMap<String, Integer> product = new HashMap();
    //订单表
    private static HashMap<String, String> orders = new HashMap();
    //库存表
    private static HashMap<String, Integer> stock = new HashMap();

    static {
        product.put("123", 10000);
        stock.put("123", 10000);
    }

    public String select_info(String product_id) {
        log.info("限量抢购商品XXX共" + product.get(product_id) + ",现在成功下单" + orders.size()
                + ",剩余库存" + stock.get(product_id) + "件");
        return "限量抢购商品XXX共" + product.get(product_id) + ",现在成功下单" + orders.size()
                + ",剩余库存" + stock.get(product_id) + "件";
    }

    /**
     * 高并发有问题
     */
    public String order1(String product_id) {
        if (stock.get(product_id) == 0) {
            log.info("活动已经结束");
            return "活动已经结束了";
            //已近买完了
        }
        //还没有卖完
        try {
            //模拟操作数据库
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        orders.put(String.valueOf(UUID.randomUUID()), product_id);
        stock.put(product_id, stock.get(product_id) - 1);
        log.info(stock.get("剩下了" + stock.get(product_id)));

        return select_info(product_id);
    }


    /**
     * 高并发没问题，但是效率低下
     */
    public synchronized String order2(String product_id) {
        if (stock.get(product_id) == 0) {
            log.info("活动已经结束");
            return "活动已经结束了";
            //已近买完了
        }
        //还没有卖完
        try {
            //模拟操作数据库
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        orders.put(String.valueOf(UUID.randomUUID()), product_id);
        stock.put(product_id, stock.get(product_id) - 1);
        log.info(stock.get("剩下了" + stock.get(product_id)));

        return select_info(product_id);
    }

    /**
     * 高并发没问题，效率还行
     */
    public String order3(String product_id) throws Exception {
        /**
         * redis加锁
         */
        String value = System.currentTimeMillis() + 10000 + "";
        if (!redisLock.lock3(product_id, value)) {
            log.error("======================当前业务繁忙,请稍后再试==================");
            return "当前业务繁忙,请稍后再试";
        }
        if (stock.get(product_id) == 0) {
            log.info("活动已经结束");
            return "活动已经结束了";
            //已近买完了
        }
        //还没有卖完
        try {
            //模拟操作数据库
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        orders.put(String.valueOf(UUID.randomUUID()), product_id);
        stock.put(product_id, stock.get(product_id) - 1);
        log.info(stock.get("剩下了" + stock.get(product_id)));

        // redis解锁
        redisLock.unlock(product_id, value);
        return select_info(product_id);
    }
}