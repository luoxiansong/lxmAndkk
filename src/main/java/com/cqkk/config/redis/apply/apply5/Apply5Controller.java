package com.cqkk.config.redis.apply.apply5;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: lxmAndkk
 * @description: 测试模拟Controller
 * @author: luo kk
 * @create: 2021-07-08 06:39
 */
@RestController
@RequestMapping("/apply5")
public class Apply5Controller {

    @Resource
    private Apply5Service apply5Service;

    @RequestMapping("/select_info")
    public String select_info(String product_id) {
        return apply5Service.select_info(product_id);
    }

    @RequestMapping("/order")
    public String order(String product_id) throws Exception {
        return apply5Service.order3(product_id);
    }
}