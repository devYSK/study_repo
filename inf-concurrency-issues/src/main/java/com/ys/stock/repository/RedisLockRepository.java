package com.ys.stock.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author : ysk
 */
@Component
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String, String> redisTemplate;


    public Boolean lock(Long key) {
        return redisTemplate.opsForValue().setIfAbsent(generateKey(key),
                "lock", Duration.ofMillis(3_000));
    }

    public Boolean unLock(Long key) {
        System.out.println("RedisLockRepository.unLock");
        return redisTemplate.delete(generateKey(key));
    }

    private String generateKey(Long key) {
        return key.toString();
    }
}
