package com.ys.demo.serivce;

import com.ys.demo.model.UserEntity;
import com.ys.demo.pesistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if (userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }

        final String email = userEntity.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(userEntity);
    }


    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {

        UserEntity originalUser = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new UsernameNotFoundException("not found");
        });
        System.out.println("UserService.getByCredentials");
        if (encoder.matches(password, originalUser.getPassword())) {
            log.info("matched!");
            return originalUser;
        }
        else {
            log.info("not match return null");
            return null;
        }
    }
}
