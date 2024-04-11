package com.example.reactive.domain.user.dto;

import java.time.LocalDateTime;

import com.example.reactive.domain.post.Post;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPostResponse {
    private Long id;
    private String username;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserPostResponse of(Post post) {
        return UserPostResponse.builder()
                .id(post.getId())
                .username(post.getUser().getName())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
