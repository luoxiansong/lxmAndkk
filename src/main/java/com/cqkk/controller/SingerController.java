package com.cqkk.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cqkk.entity.MyPage;
import com.cqkk.entity.Singer;
import com.cqkk.service.SingerService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: lxmAndkk
 * @description: Controller Singer
 * @author: luo kk
 * @create: 2021-06-10 20:59
 */
@Controller
@Api(tags = "Singer Controller")
@RequestMapping("/singer")
@ResponseBody
public class SingerController {

    private Log log = LogFactory.getLog(SingerController.class);

    @Resource
    private SingerService singerService;

    @RequestMapping(value = "/getSinger", method = RequestMethod.GET)
    @ApiOperation(value = "getSinger", notes = "获取歌手信息")
    public String getSinger(@RequestParam("singerNm") String singerNm,
                            @RequestParam("singerAge") int singerAge,
                            @RequestParam("current") int current,
                            @RequestParam("size") int size,
                            @RequestParam("singerSex") String singerSex) {
        log.info("SingerController=======>getSingInfo");
        HashMap<String, Object> map = new HashMap<>();
        Singer singer = new Singer();
        singer.setSingerNm(singerNm);
        singer.setAge(singerAge);
        singer.setSingerSex(singerSex);
        MyPage<Singer> singerList = singerService.getSingerBySinger(singer, current, size);
        map.put("result", singerList);
        map.put("status", "200");
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    @RequestMapping(value = "/getSingerById", method = RequestMethod.GET)
    @ApiOperation(value = "getSingerById", notes = "根据歌手ID获取歌手信息")
    public String getSingerById(@RequestParam(value = "id") Integer singerId) {
        log.info("SingerController=======>getSingerInfoById");
        HashMap<String, Object> map = new HashMap<>();
        Singer singer = singerService.getSingerById(singerId);
        System.out.println(singer);
        map.put("result", singer);
        map.put("status", "200");
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    @RequestMapping(value = "/getSingerByNm", method = RequestMethod.GET)
    @ApiOperation(value = "getSingerByNm", notes = "根据歌手姓名获取歌手信息")
    public String getSingerByNm(@RequestParam(value = "name") String singerNm) {
        log.info("SingerController=======>getSingerByNm");
        HashMap<String, Object> map = new HashMap<>();
        List<Singer> singers = singerService.getSingerByNm(singerNm);
        singers.forEach(System.out::println);
        map.put("result", singers);
        map.put("status", "200");
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    /**
     * map 所有非空属性等于 =
     * <p>
     * //condition   执行条件
     * //params      map 类型的参数, key 是字段名, value 是字段值
     * //null2IsNull 是否参数为 null 自动执行 isNull 方法, false 则忽略这个字段\
     *
     * @return children
     */
    @RequestMapping(value = "/getSingerAllEq", method = RequestMethod.GET)
    @ApiOperation(value = "getSingerAllEq", notes = "根据传入参数全匹配alleq")
    public String getSingerAllEq(@RequestParam("singerNm") String singerNm,
                                 @RequestParam("singerId") int singerId,
                                 @RequestParam("singerAge") int singerAge) {
        log.info("SingerController=======>getSingerAllEq");
        HashMap<String, Object> map = new HashMap<>();
        map.put("SINGER_NM", singerNm);
        map.put("SINGER_ID", singerId);
        map.put("AGE", singerAge);
        map.put("SINGER_PATH", null);
        map.put("LUOKAKA", null);//测试排除不是条件的字段 在后面impl里
        List<Singer> singers = singerService.getSingerAllEq(map);
        map.clear();
        singers.forEach(System.out::println);
        map.put("result", singers);
        map.put("status", "200");
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    @RequestMapping(value = "/getSingerByAge", method = RequestMethod.GET)
    @ApiOperation(value = "getSingerByAge", notes = "根据歌手年纪区间获取歌手信息")
    public String getSingerByAge(@RequestParam(value = "startAge") Integer startAge,
                                 @RequestParam(value = "endAge") Integer endAge) {
        log.info("SingerController=======>getSingerByAge");
        HashMap<String, Object> map = new HashMap<>();
        List<Map<String, Object>> singers = singerService.getSingerByAge(startAge, endAge);
        for (Map<String, Object> map1 : singers) {
            System.out.println(map1.get("SINGER_NM"));
        }
        singers.forEach(System.out::println);
        map.put("result", singers);
        map.put("status", "200");
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    @RequestMapping(value = "/saveSinger", method = RequestMethod.POST)
    @ApiOperation(value = "saveSinger", notes = "新增歌手信息")
    public String saveSinger(@RequestBody Singer singer) {
        log.info("SingerController=======>saveSinger");
        HashMap<String, Object> map = new HashMap<>();
        Singer newSinger = new Singer();
        newSinger.setSingerNm(singer.getSingerNm());
        newSinger.setSingerSex(singer.getSingerSex());
        newSinger.setSingerPath(singer.getSingerPath());
        newSinger.setAge(singer.getAge());
        singerService.save(newSinger);//自带的 无需再写
        map.put("result", "新增成功");
        map.put("status", "200");
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    @RequestMapping(value = "/batchSaveSinger", method = RequestMethod.POST)
    @ApiOperation(value = "batchSaveSinger", notes = "批量新增歌手信息")
    public String batchSaveSinger(@RequestParam("singerNm") String singerNm,
                                  @RequestParam("singerSex") String singerSex,
                                  @RequestParam("singerAge") int singerAge,
                                  @RequestParam("singerPath") String singerPath) {
        log.info("SingerController=======>batchSaveSinger");
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Singer> singers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Singer singer = new Singer();
            singer.setSingerNm(singerNm + i);
            singer.setSingerSex(singerSex);
            singer.setSingerPath(singerPath + i);
            singer.setAge(singerAge + i);
            singers.add(singer);
        }

        singerService.saveBatch(singers);
        map.put("result", "批量新增成功");
        map.put("status", "200");
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    @RequestMapping(value = "/modSinger", method = RequestMethod.POST)
    @ApiOperation(value = "modSinger", notes = "修改歌手信息")
    public String modSinger(@RequestParam("singerId") Integer singerId,
                            @RequestParam("singerNm") String singerNm,
                            @RequestParam("singerSex") String singerSex,
                            @RequestParam("singerAge") int singerAge,
                            @RequestParam("singerPath") String singerPath) {
        log.info("SingerController=======>modSinger");
        HashMap<String, Object> map = new HashMap<>();
        Singer singer = new Singer();
        singer.setSingerId(singerId);
        singer.setSingerNm(singerNm);
        singer.setSingerSex(singerSex);
        singer.setSingerPath(singerPath);
        singer.setAge(singerAge);

        singerService.updateById(singer);//自带的
        map.put("result", "修改成功");
        map.put("status", "200");
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    @RequestMapping(value = "/batchModSinger", method = RequestMethod.POST)
    @ApiOperation(value = "batchModSinger", notes = "批量修改歌手信息")
    public String batchModSinger(@RequestParam("singerId") Integer singerId,
                                 @RequestParam("singerNm") String singerNm,
                                 @RequestParam("singerSex") String singerSex,
                                 @RequestParam("singerAge") int singerAge,
                                 @RequestParam("singerPath") String singerPath) {
        log.info("SingerController=======>batchModSinger");
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Singer> singers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Singer singer = new Singer();
            singer.setSingerNm(singerNm + i);
            singer.setSingerId(singerId + i);
            singer.setSingerSex(singerSex);
            singer.setSingerPath(singerPath + i);
            singer.setAge(singerAge + i);
            singers.add(singer);
        }

        singerService.updateBatchById(singers);
        map.put("result", "批量修改成功");
        map.put("status", "200");
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    @RequestMapping(value = "/deleteSinger/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "delSinger", notes = "根据id删除信息")
    public String delSinger(@PathVariable("id") String singId) {
        log.info("SingerController=======>delSinger");
        HashMap<String, Object> map = new HashMap<>();
        singerService.removeById(singId);
        map.put("code", "200");
        map.put("msg", "删除成功");
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    @RequestMapping(value = "/deleteBathSinger", method = RequestMethod.GET)
    @ApiOperation(value = "deleteBathSinger", notes = "批量删除信息")
    public String deleteBathSinger(@RequestParam("id") Integer singId) {
        log.info("SingerController=======>deleteBathSinger");
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            integers.add(singId + i);
        }
        singerService.removeByIds(integers);
        map.put("code", "200");
        map.put("msg", "批量删除成功");
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    @RequestMapping(value = "/getSingerInfoByRowBounds", method = RequestMethod.GET)
    @ApiOperation(value = "getSingerInfoByRowBounds", notes = "使用mybatis自带的分页rowbounds查询信息")
    public String getSingerInfoByRowBounds(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        log.info("SingerController=======>getSingerInfoByRowBounds");
        HashMap<String, Object> map = new HashMap<>();
        List<Singer> singerList = singerService.getSingerInfoByRowBounds(pageNum, pageSize);
        map.put("code", "200");
        map.put("result", singerList);
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    @RequestMapping(value = "/getSingerInfoByPageHelper", method = RequestMethod.GET)
    @ApiOperation(value = "getSingerInfoByPageHelper", notes = "使用分页插件PageHelper查询信息")
    public String getSingerInfoByPageHelper(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        log.info("SingerController=======>getSingerInfoByPageHelper");
        //    "pageNum":1,    //当前页码
        //    "pageSize":50,	//每页个数
        //    "size":1,		//当前页个数
        //    "startRow":1,	//由第几条开始
        //    "endRow":1,		//到第几条结束
        //    "total":1,		//总条数
        //    "pages":1,		//总页数
        //    "list":[{"dateTime":"2020-03-21","operationType":1}],//查出出来的数据集合
        //    "prePage":0,				//上一页
        //    "nextPage":0,				//下一页
        //    "isFirstPage":true,			//是否为首页
        //    "isLastPage":true,			//是否为尾页
        //    "hasPreviousPage":false,	//是否有上一页
        //    "hasNextPage":false,		//是否有下一页
        //    "navigatePages":8,			//每页显示的页码个数
        //    "navigatepageNums":[1],		//首页
        //    "navigateFirstPage":1,		//尾页
        //    "navigateLastPage":1,		//页码数
        //    "firstPage":1,
        //    "lastPage":1
        HashMap<String, Object> map = new HashMap<>();
        PageInfo<Singer> pageInfo = singerService.getSingerInfoByPageHelper(pageNum, pageSize);
        MyPage<Singer> singerMyPage = new MyPage<>();
        singerMyPage.setLists(pageInfo.getList());
        singerMyPage.setTotal(pageInfo.getTotal());
        singerMyPage.setCurrent(pageInfo.getPageNum());
        singerMyPage.setSize(pageInfo.getSize());
        map.put("code", "200");
        map.put("result", singerMyPage);
        return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }
}