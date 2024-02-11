package com.fastcampus.helloecommerceservice.controller.customer;

import com.fastcampus.helloecommerceservice.controller.dto.customer.CustomerDTO;
import com.fastcampus.helloecommerceservice.controller.dto.customer.CustomerUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CustomerController {

    // TODO 회원 정보 조회
    @GetMapping(value = "/customer/{customerId}")
    public CustomerDTO get(@PathVariable String customerId) {
        return null;
    }

    // TODO 회원 수정
    @PostMapping(value = "/customer/update")
    public CustomerDTO update(@RequestBody CustomerUpdateDTO customerUpdateDTO) {
        return null;
    }

}
