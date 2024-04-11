package com.example.hblockhound.service;

import java.time.Duration;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import com.example.hblockhound.repository.User;
import com.example.hblockhound.repository.UserR2dbcRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
//    private final UserRepository userRepository;
    private final UserR2dbcRepository userR2dbcRepository;
    private final ReactiveRedisTemplate<String, User> reactiveRedisTemplate;

    public Mono<User> create(String name, String email) {
        return userR2dbcRepository.save(User.builder().name(name).email(email).build());
    }

    public Flux<User> findAll() {
        return userR2dbcRepository.findAll();
    }

    private String getUserCacheKey(Long id) {
        return "users:%d".formatted(id);
    }

    public Mono<User> findById(Long id) {
        return reactiveRedisTemplate.opsForValue()
                .get(getUserCacheKey(id))
                .switchIfEmpty(userR2dbcRepository.findById(id)
                        .flatMap(u -> reactiveRedisTemplate.opsForValue()
                                .set(getUserCacheKey(id), u, Duration.ofSeconds(30))
                                .then(Mono.just(u)))
                );
    }

    public Mono<Void> deleteById(Long id) {
        return userR2dbcRepository.deleteById(id)
                .then(reactiveRedisTemplate.unlink(getUserCacheKey(id)))
                .then(Mono.empty());
    }

    public Mono<Void> deleteByName(String name) {
        return userR2dbcRepository.deleteByName(name);
    }

    public Mono<User> update(Long id, String name, String email) {
        return userR2dbcRepository.findById(id)
                .flatMap(u -> {
                    u.setName(name);
                    u.setEmail(email);
                    return userR2dbcRepository.save(u);
                })
                .flatMap(u -> reactiveRedisTemplate.unlink(getUserCacheKey(id)).then(Mono.just(u)));
    }
}
