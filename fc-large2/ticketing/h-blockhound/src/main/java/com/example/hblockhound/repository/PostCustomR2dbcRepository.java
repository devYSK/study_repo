package com.example.hblockhound.repository;

import reactor.core.publisher.Flux;

public interface PostCustomR2dbcRepository {
    Flux<Post> findAllByUserId(Long userId);
}

