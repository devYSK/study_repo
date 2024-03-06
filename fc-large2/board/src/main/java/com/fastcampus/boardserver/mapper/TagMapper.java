package com.fastcampus.boardserver.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.fastcampus.boardserver.dto.TagDTO;

@Mapper
public interface TagMapper {
    public int register(TagDTO tagDTO);

    public void updateTags(TagDTO tagDTO);

    public void deletePostTag(int tagId);
}
