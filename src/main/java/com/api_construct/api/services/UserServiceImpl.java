package com.api_construct.api.services;

import com.api_construct.api.models.User;
import com.api_construct.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(String id, String pwd) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent() && foundUser.get().getPwd().equals(pwd)) {
            return foundUser.get(); // 返回找到的用户
        }
        return null; // 如果未找到用户或密码不匹配，返回null
    }

    @Override
    public User register(User user) {
        if (userRepository.existsById(user.getId())) {
            return null; // 用户已存在
        }
        return userRepository.save(user); // 保存新用户
    }

    @Override
    public User logout(String id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            user.setStatus("Off");
            return userRepository.save(user); // 更新用户状态为 Off
        }
        return null;
    }

    @Override
    public User update(User user) {
        if (userRepository.existsById(user.getId())) {
            return userRepository.save(user); // 更新用户信息
        }
        return null;
    }
}
