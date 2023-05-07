package com.ys.cafekiosk.spring.api.service.order.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ys.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.ys.cafekiosk.spring.domain.order.Order;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderCreateResponse {

	private Long id;
	private int totalPrice;
	private LocalDateTime registeredDateTime;
	private List<ProductResponse> products;

	@Builder
	private OrderCreateResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> products) {
		this.id = id;
		this.totalPrice = totalPrice;
		this.registeredDateTime = registeredDateTime;
		this.products = products;
	}

	public static OrderCreateResponse of(Order order) {
		return OrderCreateResponse.builder()
			.id(order.getId())
			.totalPrice(order.getTotalPrice())
			.registeredDateTime(order.getRegisteredDateTime())
			.products(order.getOrderProducts().stream()
				.map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
				.collect(Collectors.toList())
			)
			.build();
	}

}

