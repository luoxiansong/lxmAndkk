package com.cqkk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqkk.entity.Sing;
import com.cqkk.mapper.SingMapper;
import com.cqkk.service.SingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SingServiceImpl implements SingService {

    @Resource
    private SingMapper singMapper;

    @Override
    public List<Sing> getSingListBySingerId(Integer SingerId) {
        QueryWrapper<Sing> wrapper = new QueryWrapper<>();
        wrapper.eq("SINGER_ID", SingerId).orderByAsc("SING_ID");
        return singMapper.selectList(wrapper);
    }
}
