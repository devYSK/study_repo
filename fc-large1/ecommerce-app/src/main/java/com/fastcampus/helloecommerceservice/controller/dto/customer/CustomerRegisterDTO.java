package com.fastcampus.helloecommerceservice.controller.dto.customer;

import lombok.Data;

@Data(staticConstructor = "of")
public class CustomerRegisterDTO {
    private String username;
    private String phoneNumber;
    private int age;
    private String address;
    private String email;
    private String password1;
    private String password2;
}
