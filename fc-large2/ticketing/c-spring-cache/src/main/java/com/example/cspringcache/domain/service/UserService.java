package com.example.cspringcache.domain.service;

import java.time.Duration;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.cspringcache.config.CacheConfig;
import com.example.cspringcache.domain.entity.RedisHashUser;
import com.example.cspringcache.domain.entity.User;
import com.example.cspringcache.domain.repository.RedisHashUserRepository;
import com.example.cspringcache.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisHashUserRepository redisHashUserRepository;

    private final RedisTemplate<String, User> userRedisTemplate;
    private final RedisTemplate<String, Object> objectRedisTemplate;

    public User getUser(final Long id) {
        var key = "users:%d".formatted(id);
        var cachedUser = objectRedisTemplate.opsForValue().get(key);

        if (cachedUser != null) {
            return (User) cachedUser;
        }

        User user = userRepository.findById(id).orElseThrow();

        objectRedisTemplate.opsForValue().set(key, user, Duration.ofSeconds(30));

        return user;
    }

    public RedisHashUser getUser2(final Long id) {
        return redisHashUserRepository.findById(id).orElseGet(() -> {

            User user = userRepository.findById(id).orElseThrow();

            return redisHashUserRepository.save(RedisHashUser.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build());
        });
    }

    @Cacheable(cacheNames = CacheConfig.CACHE_NAME1, key = "'user:' + #id")
    public User getUser3(final Long id) {
        return userRepository.findById(id).orElseThrow();
    }

}
