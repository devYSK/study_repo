package com.ys.cafekiosk.spring.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ys.cafekiosk.spring.domain.BaseEntity;
import com.ys.cafekiosk.spring.domain.orderproduct.OrderProduct;
import com.ys.cafekiosk.spring.domain.product.Product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private int totalPrice;

	private LocalDateTime registeredDateTime;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderProduct> orderProducts = new ArrayList<>();

	@Builder
	private Order(List<Product> products, OrderStatus orderStatus, LocalDateTime registeredDateTime) {
		this.orderStatus = orderStatus;
		this.totalPrice = calculateTotalPrice(products);
		this.registeredDateTime = registeredDateTime;
		this.orderProducts = products.stream()
			.map(product -> new OrderProduct(this, product))
			.collect(Collectors.toList());
	}

	public static Order create(List<Product> products, LocalDateTime registeredDateTime) {
		return Order.builder()
			.orderStatus(OrderStatus.INIT)
			.products(products)
			.registeredDateTime(registeredDateTime)
			.build();
	}

	private int calculateTotalPrice(List<Product> products) {
		return products.stream()
			.mapToInt(Product::getPrice)
			.sum();
	}
}
