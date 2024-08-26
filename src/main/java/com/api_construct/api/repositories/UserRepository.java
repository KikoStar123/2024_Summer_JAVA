package com.api_construct.api.repositories;

import com.api_construct.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    // 可以根据需要添加自定义查询方法
}
