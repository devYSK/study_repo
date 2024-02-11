package com.fastcampus.ecommerce.admin.service.dto;

import lombok.Data;

@Data
public class AdminUserFormDTO {
    private String username;
    private String password1;
    private String password2;
    private String email;
}
