package com.nhnent.forward.mybatistojpa.controller;

import com.nhnent.forward.mybatistojpa.model.Item;
import com.nhnent.forward.mybatistojpa.model.Page;
import com.nhnent.forward.mybatistojpa.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    ItemService itemService;


    // NOTE #20 : ItemController에서의 pagination 구현 (page 파라미터)
    @GetMapping("")
    public List<Item> getItems(@RequestParam(defaultValue = "1") int page) {
        if (page < 1) {
            page = 1;
        }

        return itemService.getItems(page, Page.PAGE_SIZE);
    }

    @GetMapping("/{itemId}")
    public Item getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @PostMapping("")
    public Item createItem(@RequestBody Item item) {
        return itemService.createItem(item);
    }

    @PutMapping("/{itemId}")
    public Item updateItem(@PathVariable Long itemId, @RequestBody Item item) {
        item.setItemId(itemId);
        return itemService.updateItem(item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
    }

}
