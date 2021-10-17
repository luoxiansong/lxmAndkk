package com.cqkk.service;

import com.cqkk.entity.UserInfo;

import java.util.List;

/**
 * @program: lxmAndkk
 * @description: UserService用户Service
 * @author: luo kk
 * @create: 2021-05-31 21:22
 */
public interface UserService {

    /*获取用户信息*/
    List<UserInfo> queryUserInfo(UserInfo userInfo);
}
