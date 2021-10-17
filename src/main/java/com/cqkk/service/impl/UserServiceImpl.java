package com.cqkk.service.impl;

import com.cqkk.entity.UserInfo;
import com.cqkk.mapper.UserInfoMapper;
import com.cqkk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @program: lxmAndkk
 * @description: UserService实现类用户UserServiceImpl
 * @author: luo kk
 * @create: 2021-05-31 21:22
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfo> queryUserInfo(UserInfo userInfo) {
        HashMap<String, Object> map = new HashMap<>();
        return userInfoMapper.queryUserInfo(userInfo);
    }

}
