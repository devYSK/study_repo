package com.vinsguru.redisperformance.service.util;

import reactor.core.publisher.Mono;

public abstract class CacheTemplate<KEY, ENTITY> {

    public Mono<ENTITY> get(KEY key){
        return getFromCache(key)
                    .switchIfEmpty(
                            getFromSource(key)
                                    .flatMap(e -> updateCache(key, e))
                    );
    }

    public Mono<ENTITY> update(KEY key, ENTITY entity){
        return updateSource(key, entity)
                    .flatMap(e -> deleteFromCache(key).thenReturn(e));
    }

    public Mono<Void> delete(KEY key){
        return deleteFromSource(key)
                    .then(deleteFromCache(key));
    }

    protected abstract Mono<ENTITY> getFromSource(KEY key);

    protected abstract Mono<ENTITY> getFromCache(KEY key);

    protected abstract Mono<ENTITY> updateSource(KEY key, ENTITY entity);

    protected abstract Mono<ENTITY> updateCache(KEY key, ENTITY entity);

    protected abstract Mono<Void> deleteFromSource(KEY key);

    protected abstract Mono<Void> deleteFromCache(KEY key);

}
