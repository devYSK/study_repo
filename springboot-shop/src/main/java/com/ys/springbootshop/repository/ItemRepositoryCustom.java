package com.ys.springbootshop.repository;

import com.ys.springbootshop.dto.ItemSearchDto;
import com.ys.springbootshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : ysk
 */
public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
