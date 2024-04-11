package com.example.cspringcache.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.cspringcache.domain.entity.RedisHashUser;

public interface RedisHashUserRepository extends CrudRepository<RedisHashUser, Long> {
}
