package com.ys.springbootshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author : ysk
 */
@Controller
public class ItemController {

    @GetMapping(value = "/admin/item/new")
    public String itemForm() {
        return "/item/itemForm";
    }

}
