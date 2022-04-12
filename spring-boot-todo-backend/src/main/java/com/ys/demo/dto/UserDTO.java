package com.ys.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class UserDTO {

    private String token;

    private String email;

    private String username;

    private String password;

    private String id;

}
