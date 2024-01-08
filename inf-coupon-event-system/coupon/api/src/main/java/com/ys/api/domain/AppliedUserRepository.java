package com.ys.api.domain;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppliedUserRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public AppliedUserRepository(final RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;

	}
	public Long add(Long userId) {
		return redisTemplate.opsForSet()
			.add("applied_user", userId.toString());
	}
}
