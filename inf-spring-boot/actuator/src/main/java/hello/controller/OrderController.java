package hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.order.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class OrderController {
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping("/order")
	public String order() {
		log.info("order");
		orderService.order();
		return "order";
	}

	@GetMapping("/cancel")
	public String cancel() {
		log.info("cancel");
		orderService.cancel();
		return "cancel";
	}

	@GetMapping("/stock")
	public int stock() {
		log.info("stock");
		return orderService.getStock().get();
	}
}