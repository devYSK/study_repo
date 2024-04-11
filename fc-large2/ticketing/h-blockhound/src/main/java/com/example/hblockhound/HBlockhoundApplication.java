package com.example.hblockhound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.blockhound.BlockHound;

@SpringBootApplication
public class HBlockhoundApplication {

	public static void main(String[] args) {
		BlockHound.install();

		SpringApplication.run(HBlockhoundApplication.class, args);
	}

}
