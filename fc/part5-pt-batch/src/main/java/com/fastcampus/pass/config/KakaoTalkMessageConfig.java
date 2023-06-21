package com.fastcampus.pass.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kakaotalk")
public class KakaoTalkMessageConfig {
	private String host;
	private String token;
}