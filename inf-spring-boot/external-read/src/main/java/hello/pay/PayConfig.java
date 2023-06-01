package hello.pay;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class PayConfig {

	@Bean
	@Profile("default") // 해당 프로필이 활성화된 경우에만 빈을 등록한다.
	public LocalPayClient localPayClient() {
		log.info("LocalPayClient 빈 등록");
		return new LocalPayClient();
	}

	@Bean
	@Profile("prod") // 해당 프로필이 활성화된 경우에만 빈을 등록한다.
	public ProdPayClient prodPayClient() {
		log.info("ProdPayClient 빈 등록");
		return new ProdPayClient();
	}
}