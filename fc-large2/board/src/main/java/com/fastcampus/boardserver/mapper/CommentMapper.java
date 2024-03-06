package com.fastcampus.boardserver.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.fastcampus.boardserver.dto.CommentDTO;

@Mapper
public interface CommentMapper {
    public int register(CommentDTO commentDTO);

    public void updateComments(CommentDTO commentDTO);

    public void deletePostComment(int commentId);
}
