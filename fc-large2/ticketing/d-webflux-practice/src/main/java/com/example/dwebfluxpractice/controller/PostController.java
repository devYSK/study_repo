package com.example.dwebfluxpractice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dwebfluxpractice.dto.PostResponse;
import com.example.dwebfluxpractice.service.PostService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public Mono<PostResponse> getPostContent(@PathVariable Long id) {
        return postService.getPostContent(id);
    }

    @GetMapping("/search")
    public Flux<PostResponse> getMultiplePostContent(@RequestParam(name = "ids") List<Long> idList) {
//        return postService.getMultiplePostContent(idList);
        return postService.getParallelMultiplePostContent(idList);
    }

}








