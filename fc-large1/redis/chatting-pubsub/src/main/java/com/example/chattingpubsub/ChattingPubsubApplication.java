package com.example.chattingpubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChattingPubsubApplication implements CommandLineRunner {

	@Autowired
	private ChatService chatService;

	public static void main(String[] args) {
		SpringApplication.run(ChattingPubsubApplication.class, args);
	}


	@Override
	public void run(String... args) {
		System.out.println("Application started..");

		chatService.enterCharRoom("chat1");
	}

}
