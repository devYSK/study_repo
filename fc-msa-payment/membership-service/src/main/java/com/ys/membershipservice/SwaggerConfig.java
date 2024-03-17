package com.ys.membershipservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
// @EnableSwagger
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("멤버십 프로젝트 API")
				.description("멤버십 설명")
				.version("1.0.0"));
	}

	// in yml
	// see: https://springdoc.org/#properties
	// springdoc:
	//   show-login-endpoint: true
	// @Bean
	// public OpenAPI openAPI() {
	// 	return new OpenAPI()
	// 		.info(new Info()
	// 			.title("커머스 프로젝트 API")
	// 			.description("상품을 등록하고, 상품을 장바구니에 담는 기능을 제공합니다.")
	// 			.version("1.0.0"))
	// 		.components(new Components()
	// 			.addSecuritySchemes("bearer-key",
	// 				new io.swagger.v3.oas.models.security.SecurityScheme()
	// 					.type(Type.HTTP)
	// 					.scheme("bearer")
	// 					.bearerFormat("JWT")));
	//
	// }
}
