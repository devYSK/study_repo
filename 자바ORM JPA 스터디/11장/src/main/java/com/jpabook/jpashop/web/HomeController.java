package com.jpabook.jpashop.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(Model model) {

        return "home";
    }

    @ResponseBody
    @RequestMapping("/wait")
    public String wait(Model model) throws InterruptedException {

        Thread.sleep(1000);

        return "response";
    }

}
