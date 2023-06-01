package com.ys.mybatistojpa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ys.mybatistojpa.model.Item;

// NOTE #10 : Items 테이블에 대한 CRUD 쿼리에 맵핑되는 java interface 메쏘드
public interface ItemMapper {
    int getItemCount();

    List<Item> getItems(@Param("offset") int offset, @Param("limit") int limit);

    Item getItem(Long itemId);

    int insertItem(Item item);

    int updateItem(Item item);

    int deleteItem(Long itemId);

}
