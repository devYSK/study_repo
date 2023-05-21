package tobyspring;

import static tobyspring.MySpringApplication.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import tobyspring.hello.HelloController;
import tobyspring.hello.HelloService;
import tobyspring.hello.SimpleHelloService;

@MySpringBootAnnotation
@SpringBootApplication
public class TobyspringApplication {

	@Bean
	public ServletWebServerFactory serverFactory() {
		return new TomcatServletWebServerFactory();
	}

	public static void main(String[] args) {
		SpringApplication.run(TobyspringApplication.class, args);
	}

}
