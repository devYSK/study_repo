package com.example.cspringcache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cspringcache.domain.entity.User;
import com.example.cspringcache.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class CSpringCacheApplication implements ApplicationRunner {

	private final UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(CSpringCacheApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<User> usersToSave = Arrays.asList(
			User.builder()
				.email("greg@fastcampus.co.kr")
				.build(),
			User.builder()
				.email("tony@fastcampus.co.kr")
				.build(),
			User.builder()
				.email("bob@fastcampus.co.kr")
				.build(),
			User.builder()
				.email("ryan@fastcampus.co.kr")
				.build()
		);

		for (User user : usersToSave) {
			Optional<User> existingUser = userRepository.findByName(user.getName());
			if (existingUser.isEmpty()) {
				userRepository.save(user);
			}
		}

	}

}
