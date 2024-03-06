package com.fastcampus.boardserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fastcampus.boardserver.dto.PostDTO;

@Mapper
public interface PostMapper {
    public int register(PostDTO postDTO);

    public List<PostDTO> selectMyProducts(int accountId);

    public void updateProducts(PostDTO postDTO);

    public void deleteProduct(int postId);
}
