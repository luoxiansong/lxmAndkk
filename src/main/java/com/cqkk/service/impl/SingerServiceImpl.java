package com.cqkk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqkk.entity.MyPage;
import com.cqkk.entity.Singer;
import com.cqkk.mapper.SingerMapper;
import com.cqkk.service.SingerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: lxmAndkk
 * @description: SingerService 实现类
 * @author: luo kk
 * @create: 2021-06-10 20:50
 */
@Transactional
@Service
public class SingerServiceImpl extends ServiceImpl<SingerMapper, Singer> implements SingerService {

    @Autowired
    private SingerMapper singerMapper;

    public MyPage<Singer> getSingerBySinger(Singer singer, Integer current, Integer size) {
        MyPage<Singer> singerMyPage = new MyPage<>();
        Page<Singer> page = new Page<>(current, size);
        QueryWrapper<Singer> queryWrapper = new QueryWrapper<Singer>()
                .like(StringUtils.isNotBlank(singer.getSingerNm()), "SINGER_NM", singer.getSingerNm())
                .ge("AGE", singer.getAge()).eq("SINGER_SEX", singer.getSingerSex())
                .orderByAsc("SINGER_ID");
        singerMapper.selectPage(page, queryWrapper);
        singerMyPage.setCurrent(current);
        singerMyPage.setSize(size);
        singerMyPage.setTotal(page.getTotal());
        singerMyPage.setLists(page.getRecords());
        return singerMyPage;
    }

    @Override
    public Singer getSingerById(Integer singerId) {
        QueryWrapper<Singer> wrapper = new QueryWrapper<Singer>().eq("SINGER_ID", singerId);
        return singerMapper.selectOne(wrapper);
    }

    @Override
    public List<Singer> getSingerByNm(String singerNm) {
        QueryWrapper<Singer> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(singerNm), "SINGER_NM", singerNm);
        return singerMapper.selectList(wrapper);
    }

    @Override
    public List<Singer> getSingerAllEq(HashMap<String, Object> map) {
        QueryWrapper<Singer> wrapper = new QueryWrapper<>();
        wrapper.allEq((k, v) -> !k.equals("name"), map);//排除不是条件的字段
        return singerMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> getSingerByAge(Integer startAge, Integer endAge) {
        QueryWrapper<Singer> wrapper = new QueryWrapper<>();
        wrapper.le("AGE", endAge).ge("AGE", startAge);
        return singerMapper.selectMaps(wrapper);
    }

    @Override
    public List<Singer> getSingerInfoByRowBounds(Integer pageNum, Integer pageSize) {
        int startNum = (pageNum - 1) * pageSize;
        RowBounds rowBounds = new RowBounds(startNum, pageSize);
        return singerMapper.getSingerInfoByRowBounds(rowBounds);
    }

    @Override
    public PageInfo<Singer> getSingerInfoByPageHelper(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Singer> singers = singerMapper.selectList(new QueryWrapper<>());
        PageInfo<Singer> pageInfo = new PageInfo<>(singers);
        return pageInfo;
    }
}