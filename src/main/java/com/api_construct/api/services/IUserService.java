package com.api_construct.api.services;

import com.api_construct.api.models.User;

public interface IUserService {
    User login(String id, String pwd); // 用户登录
    User register(User user); // 用户注册
    User logout(String id); // 用户登出
    User update(User user); // 更新用户信息
}
