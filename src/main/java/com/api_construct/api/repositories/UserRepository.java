package com.api_construct.api.repositories;

import com.api_construct.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // 根据需求，可以定义额外的查询方法，例如：
    User findByIdAndPwd(String id, String pwd);
}
