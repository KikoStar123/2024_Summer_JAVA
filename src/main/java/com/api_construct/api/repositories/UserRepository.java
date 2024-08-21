package com.api_construct.api.repositories;

import com.api_construct.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // 你可以添加其他自定义查询方法，例如：
    // User findByUsername(String username);
}
