package hello;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EnvironmentCheck {
	private final Environment env;

	public EnvironmentCheck(Environment env) {
		this.env = env;
	}

	@PostConstruct
	public void init() {
		String url = env.getProperty("url");
		String username = env.getProperty("username");
		String password = env.getProperty("password");
		log.info("env url={}", url);
		log.info("env username={}", username);
		log.info("env password={}", password);
	}
}