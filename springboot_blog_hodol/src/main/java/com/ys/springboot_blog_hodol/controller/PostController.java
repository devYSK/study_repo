package com.ys.springboot_blog_hodol.controller;

import com.ys.springboot_blog_hodol.request.PostCreate;
import com.ys.springboot_blog_hodol.request.PostEdit;
import com.ys.springboot_blog_hodol.request.PostSearch;
import com.ys.springboot_blog_hodol.response.PostResponse;
import com.ys.springboot_blog_hodol.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author : ysk
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    // get post put patch delete options head trace
    private final PostService postService;

    @PostMapping("/posts")
    public Map<Object, Object> post(@RequestBody @Valid PostCreate request) {
        // 1. id만 리턴
        // entity -> response로 응
        Long postId = postService.write(request);

        return Map.of("postId", postId);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getPosts(@RequestBody PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId,
                     @RequestBody @Valid PostEdit postEdit) {
        return postService.edit(postId, postEdit);
    }


    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

}
