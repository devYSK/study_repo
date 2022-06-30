package com.ys.springbootshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author : ysk
 */
@Controller
public class MainController {

    @GetMapping(value = "/")
    public String main() {
        return "main";
    }
}
