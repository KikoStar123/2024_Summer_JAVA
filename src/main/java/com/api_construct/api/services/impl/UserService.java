package com.api_construct.api.services.impl;

import com.api_construct.api.models.User;
import com.api_construct.api.repositories.UserRepository;
import com.api_construct.api.services.BaseService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseServiceImpl<User, String> {

    public UserService(UserRepository userRepository) {
        super(userRepository);
    }

    // 如果有需要，可以在这里添加 User 特有的服务方法
}
