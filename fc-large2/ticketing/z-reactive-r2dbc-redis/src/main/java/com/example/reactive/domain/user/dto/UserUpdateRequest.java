package com.example.reactive.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateRequest {
    private String name;
    private String email;
}
