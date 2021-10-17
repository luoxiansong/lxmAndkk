package com.cqkk.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqkk.entity.Singer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SingerMapper extends BaseMapper<Singer> {

    List<Singer> getSingerInfoByRowBounds(RowBounds rowBounds);

}
