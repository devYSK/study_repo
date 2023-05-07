package com.ys.cafekiosk.spring.domain.orderproduct;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import com.ys.cafekiosk.spring.domain.BaseEntity;
import com.ys.cafekiosk.spring.domain.order.Order;
import com.ys.cafekiosk.spring.domain.product.Product;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderProduct extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	public OrderProduct(Order order, Product product) {
		this.order = order;
		this.product = product;
	}

}