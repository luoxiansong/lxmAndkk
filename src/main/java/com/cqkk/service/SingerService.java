package com.cqkk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqkk.entity.MyPage;
import com.cqkk.entity.Singer;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public interface SingerService extends IService<Singer> {
    /*根据歌手实体类获取歌手列表*/
    MyPage<Singer> getSingerBySinger(Singer singer, Integer current, Integer size);

    /*根据歌手编号获取歌手信息*/
    Singer getSingerById(Integer singerId);

    /*根据歌手姓名获取歌手信息*/
    List<Singer> getSingerByNm(String singerNm);

    /*根据歌手信息全部符合的情况*/
    List<Singer> getSingerAllEq(HashMap<String, Object> map);

    /*根据歌手年纪区间获取歌手信息*/
    List<Map<String, Object>> getSingerByAge(Integer startAge, Integer endAge);

    /*使用mybatis自带的分页rowbounds查询信息*/
    List<Singer> getSingerInfoByRowBounds(Integer pageNum, Integer pageSize);

    /*使用分页插件pagehelper*/
    PageInfo<Singer> getSingerInfoByPageHelper(Integer pageNum, Integer pageSize);

}
