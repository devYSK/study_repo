package com.fastcampus.boardserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fastcampus.boardserver.dto.PostDTO;
import com.fastcampus.boardserver.dto.request.PostSearchRequest;

@Mapper
public interface PostSearchMapper {
    public List<PostDTO> selectPosts(PostSearchRequest postSearchRequest);
}
