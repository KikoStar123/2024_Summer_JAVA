package com.api_construct.api.services;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    T save(T entity);                 // 保存或更新实体
    Optional<T> findById(ID id);      // 根据 ID 查询实体
    List<T> findAll();                // 查询所有实体
    void deleteById(ID id);           // 根据 ID 删除实体
    void delete(T entity);            // 删除实体
}
