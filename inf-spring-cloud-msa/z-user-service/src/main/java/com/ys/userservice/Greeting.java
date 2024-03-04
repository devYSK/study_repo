package com.ys.userservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@RefreshScope
@ConfigurationProperties(prefix = "token")
//@AllArgsConstructor
//@NoArgsConstructor
public class Greeting {
	@Value("${greeting.message}")
	private String message;

	@Value("${gateway.ip}")
	private String ip;

	@Value("${token.secret}")
	private String secret;
}
