package com.hodolog.api;

import com.hodolog.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class HodologApplication {

    public static void main(String[] args) {
        SpringApplication.run(HodologApplication.class, args);
    }

}
