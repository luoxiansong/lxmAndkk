package com.cqkk.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cqkk.entity.Sing;
import com.cqkk.service.SingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @program: lxmAndkk
 * @description: Controller Sing
 * @author: luo kk
 * @create: 2021-06-15 20:47
 */
@RestController
@Api(tags = "Sing Controller")
@RequestMapping("/sing")
public class SingController {

    private Log log = LogFactory.getLog(SingController.class);

    @Resource
    private SingService singService;

    @RequestMapping(value = "/getSingListBySingerId", method = RequestMethod.GET)
    @ApiOperation(value = "getSingListBySingerId", notes = "根据歌手编号获取歌曲信息")
    public String getSingListBySingerId(@RequestParam("singerId") Integer singerId) {
        log.info("根据歌手ID查询歌曲信息==========");
        HashMap<String, Object> map = new HashMap<>();
        List<Sing> sings = singService.getSingListBySingerId(singerId);
        sings.forEach(s -> System.out.println(s.toString()));
        map.put("result", sings);
        map.put("code", 200);
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }
}