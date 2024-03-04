package com.ys.userservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ys.userservice.dto.UserDto;
import com.ys.userservice.jpa.UserEntity;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);

    Iterable<UserEntity> getUserByAll();

    UserDto getUserDetailsByEmail(String userName);
}
