package com.api_construct.api.services;

import com.api_construct.api.models.User;
import com.api_construct.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(User user) {
        // 实现登录逻辑
        return userRepository.findById(user.getId()).orElse(null);
    }

    @Override
    public User register(User user) {
        // 实现注册逻辑
        return userRepository.save(user);
    }

    @Override
    public User logout(User user) {
        // 实现登出逻辑
        user.setStatus("Off");
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        // 实现更新用户信息逻辑
        return userRepository.save(user);
    }
}
