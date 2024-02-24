package com.example.cachelayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CacheLayerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheLayerApplication.class, args);
	}

}
