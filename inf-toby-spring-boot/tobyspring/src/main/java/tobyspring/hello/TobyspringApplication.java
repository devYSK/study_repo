package tobyspring.hello;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import tobyspring.config.MySpringBootApplication;

@MySpringBootApplication
// @SpringBootApplication
public class TobyspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(TobyspringApplication.class, args);
	}

}
