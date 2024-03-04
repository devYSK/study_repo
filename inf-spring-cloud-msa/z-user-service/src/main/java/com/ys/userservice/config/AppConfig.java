package com.ys.userservice.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import feign.Logger;

@Configuration
public class AppConfig {

	@Bean
	//    @LoadBalanced
	public RestTemplate getRestTemplate() {
		int TIMEOUT = 5000;

		RestTemplate restTemplate = new RestTemplateBuilder()
			.setConnectTimeout(Duration.ofMillis(TIMEOUT))
			.setReadTimeout(Duration.ofMillis(TIMEOUT))
			.build();
		//        RestTemplate restTemplate = new RestTemplate();

		return restTemplate;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
