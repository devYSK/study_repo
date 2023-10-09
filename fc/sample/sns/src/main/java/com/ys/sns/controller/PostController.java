package com.ys.sns.controller;

import com.ys.sns.controller.request.PostCommentRequest;
import com.ys.sns.controller.request.PostModifyRequest;
import com.ys.sns.controller.request.PostWriteRequest;
import com.ys.sns.controller.response.CommentResponse;
import com.ys.sns.controller.response.PostResponse;
import com.ys.sns.controller.response.Response;
import com.ys.sns.domain.user.User;
import com.ys.sns.domain.post.PostService;
import com.ys.sns.utils.ClassUtils;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostWriteRequest request, Authentication authentication) {
        postService.create(authentication.getName(), request.getTitle(), request.getBody());
        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication) {
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> myPosts(Pageable pageable, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        return Response.success(postService.my(user.getId(), pageable).map(PostResponse::fromPost));
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        return Response.success(
                PostResponse.fromPost(
                        postService.modify(user.getId(), postId, request.getTitle(), request.getBody())));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        postService.delete(user.getId(), postId);
        return Response.success();
    }

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> getComments(Pageable pageable, @PathVariable Integer postId) {
        return Response.success(postService.getComments(postId, pageable).map(CommentResponse::fromComment));
    }

    @GetMapping("/{postId}/likes")
    public Response<Integer> getLikes(@PathVariable Integer postId, Authentication authentication) {
        return Response.success(postService.getLikeCount(postId));
    }


    @PostMapping("/{postId}/comments")
    public Response<Void> comment(@PathVariable Integer postId, @RequestBody PostCommentRequest request, Authentication authentication) {
        postService.comment(postId, authentication.getName(), request.getComment());
        return Response.success();
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable Integer postId, Authentication authentication) {
        postService.like(postId, authentication.getName());
        return Response.success();
    }

}
