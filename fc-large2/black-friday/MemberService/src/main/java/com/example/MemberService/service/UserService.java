package com.example.MemberService.service;

import com.example.MemberService.entity.UserEntity;
import com.example.MemberService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserEntity registerUser(String loginId, String userName) {
        var user = new UserEntity(loginId, userName);
        return userRepository.save(user);
    }

    public UserEntity modifyUser(Long userId, String userName) {
        var user = userRepository.findById(userId).orElseThrow();
        user.userName = userName;

        return userRepository.save(user);
    }

    public UserEntity getUser(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow();
    }
}
