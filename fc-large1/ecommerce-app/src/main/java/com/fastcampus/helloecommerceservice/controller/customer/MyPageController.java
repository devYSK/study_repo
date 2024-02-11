package com.fastcampus.helloecommerceservice.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

    // TODO 구매 내역
    @GetMapping(value = {"/customer/mypage", "/customer/mypage/"})
    public String myPage() {
        return "my-page";
    }
}
