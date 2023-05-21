package tobyspring.config.autoconfig;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tobyspring.config.MyAutoConfiguration;

@MyAutoConfiguration
public class TomcatWebServerConfig {
	@Bean
	public ServletWebServerFactory servetWebServerFactory() {
		return new TomcatServletWebServerFactory();
	}

}
