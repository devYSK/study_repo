package com.ys.sns.controller.response;

import com.ys.sns.domain.post.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class LikeResponse {
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;

    public static LikeResponse fromLike(Like like) {
        return new LikeResponse(
                like.getId(),
                like.getUserId(),
                like.getUserName(),
                like.getPostId(),
                like.getRegisteredAt(),
                like.getUpdatedAt(),
                like.getRemovedAt()
        );
    }
}
