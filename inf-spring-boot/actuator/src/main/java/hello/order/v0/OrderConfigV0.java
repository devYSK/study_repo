package hello.order.v0;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.order.OrderService;

@Configuration
public class OrderConfigV0 {

	@Bean
	OrderService orderService() {
		return new OrderServiceV0();
	}

}