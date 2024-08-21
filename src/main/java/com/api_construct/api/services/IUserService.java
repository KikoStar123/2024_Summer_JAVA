package com.api_construct.api.services;

import com.api_construct.api.models.User;

public interface IUserService {
    User login(User user); // 用户登录
    User register(User user); // 用户注册
    User logout(User user); // 用户登出
    User update(User user); // 更新用户信息
}
