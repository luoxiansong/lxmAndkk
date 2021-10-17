package com.cqkk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqkk.entity.Sing;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SingMapper extends BaseMapper<Sing> {
}
