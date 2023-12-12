package com.fastcampus.projectboardadmin;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fastcampus.projectboardadmin.domain.constant.RoleType;
import com.fastcampus.projectboardadmin.dto.AdminAccountDto;
import com.fastcampus.projectboardadmin.service.AdminAccountService;

import lombok.RequiredArgsConstructor;

@ConfigurationPropertiesScan
@SpringBootApplication
@RequiredArgsConstructor
public class FastCampusProjectBoardAdminApplication {

	private final AdminAccountService service;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(FastCampusProjectBoardAdminApplication.class, args);
	}

	@PostConstruct
	public void init() {

		final var test = service.searchUser("test");

		if (test.isEmpty()) {

			service.saveUser(
				"test",
				"12345678",

				// passwordEncoder.encode("{bcrypt}" + "1234"),
				Set.of(RoleType.ADMIN),
				"test@naver.com",
				"test",
				"test"
			);

		}

	}
}
