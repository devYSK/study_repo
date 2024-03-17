package com.fastcampuspay.remittance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RemittanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RemittanceApplication.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received SIGINT (Ctrl+C)");
        }));
    }
}
