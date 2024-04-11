package com.example.cspringcache.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@RedisHash(value = "redishash-user", timeToLive = 30L)
public class RedisHashUser {
    @Id
    private Long id;
    private String name;
    @Indexed
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
