package com.cqkk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqkk.entity.MyLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LogMapper extends BaseMapper<MyLog> {
}
