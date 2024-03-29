package com.ys.mybatistojpa.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ys.mybatistojpa.entity.ItemEntity;
import com.ys.mybatistojpa.mapper.ItemMapper;
import com.ys.mybatistojpa.model.Item;
import com.ys.mybatistojpa.repository.ItemRepository;

@Service
public class ItemService {
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemRepository itemRepository;

    // NOTE #21 : pagination 구현
    public List<Item> getItems(int pageNumber, int pageSize) {
        int totalCount = itemMapper.getItemCount();

        int pageOffset = (pageNumber - 1) * pageSize;
        if (pageOffset >= totalCount) {
            return Collections.emptyList();
        }

        return itemMapper.getItems(pageOffset, pageSize);
    }

    // NOTE #18 : 어플리케이션에서의 사용
    public Item getItem(Long itemId) {
        return itemMapper.getItem(itemId);
    }

    @Transactional
    public Item createItem(Item item) {
        int count = itemMapper.insertItem(item);
        if (count != 1) {
            throw new RuntimeException("can't create item");
        }

        return item;
    }

    @Transactional
    public Item updateItem(Item item) {
        int count = itemMapper.updateItem(item);
        if (count != 1) {
            throw new RuntimeException("can't update item");
        }

        return getItem(item.getItemId());
    }

    @Transactional
    public boolean deleteItem(Long itemId) {
        return (itemMapper.deleteItem(itemId) == 1);
    }

    public List<Item> getItems(Pageable pageable) {
        Page<ItemEntity> itemPage = itemRepository.findAll(pageable);

        return itemPage.getContent()
            .stream()
            .map(ItemEntity::toItemDto)
            .toList();
    }

}
