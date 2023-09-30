package com.ys.jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
	basePackages = {"com.ys.common.domain", "com.ys.jpa"}
)
@EntityScan(
	basePackages = {"com.ys.common.domain"}
)
public class JpaConfig {
}
