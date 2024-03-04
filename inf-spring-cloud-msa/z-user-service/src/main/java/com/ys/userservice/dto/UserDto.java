package com.ys.userservice.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String decryptedPwd;

    private String encryptedPwd;

    private List<ResponseOrder> orders;

    private List<ResponseCatalog> catalogs;

}
