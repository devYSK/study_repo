package com.fastcampus.ecommerce.admin.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class HttpClientConfig {
	public static final int LIMIT_SECONDS = 3;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
			.setConnectTimeout(Duration.ofSeconds(LIMIT_SECONDS))
			.setReadTimeout(Duration.ofSeconds(LIMIT_SECONDS))
			.build();
	}

}
