package com.example.reactive.domain.post;

import reactor.core.publisher.Flux;

public interface PostCustomR2dbcRepository {
    Flux<Post> findAllByUserId(Long userId);
}

