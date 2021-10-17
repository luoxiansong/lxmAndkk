package com.cqkk.mapper;

import com.cqkk.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface UserInfoMapper {

    //新增用户
    void addUser(@Param("user") UserInfo userInfo);

    //修改用户
    void updateUser(@Param("user") UserInfo userInfo);

    //删除用户
    void deleteUser(@Param("id") Integer id);

    //根据map查询用户信息list
    List<UserInfo> queryUserInfo(@Param("user") UserInfo userInfo);

}
