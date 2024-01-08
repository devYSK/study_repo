package com.ys.api.domain;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CouponCountRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public Long increment() {
		final ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();

		return stringStringValueOperations
			.increment("coupon_count")
			;
	}
}
