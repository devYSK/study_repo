package hello.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "hello.core")
public class HelloModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloModuleApplication.class, args);
    }
}
