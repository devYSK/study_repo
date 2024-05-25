package com.example.SearchService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    // key -> value
    // tag(keyword) -> 1, 2, 3, 4, 5, 6 (Set)

    public void addTagCache(Long productId, List<String> tags) {
        var ops = stringRedisTemplate.opsForSet();

        tags.forEach( tag -> {
            ops.add(tag, productId.toString());
        });
    }

    public void removeTagCache(Long productId, List<String> tags) {
        var ops = stringRedisTemplate.opsForSet();

        tags.forEach( tag -> {
            ops.remove(tag, productId.toString());
        });
    }

    public List<Long> getProductIdsByTag(String tag) {
        var ops = stringRedisTemplate.opsForSet();

        var values = ops.members(tag);
        if(values != null) {
            return values.stream().map(Long::parseLong).toList();
        }

        return Collections.emptyList();
    }
}
