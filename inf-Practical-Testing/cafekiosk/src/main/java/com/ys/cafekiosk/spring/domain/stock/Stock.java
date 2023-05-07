package com.ys.cafekiosk.spring.domain.stock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ys.cafekiosk.spring.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productNumber;

	private int quantity;

	@Builder
	private Stock(String productNumber, int quantity) {
		this.productNumber = productNumber;
		this.quantity = quantity;
	}

	public static Stock create(String productNumber, int quantity) {
		return Stock.builder()
			.productNumber(productNumber)
			.quantity(quantity)
			.build();
	}

	public boolean isQuantityLessThan(int quantity) {
		return this.quantity < quantity;
	}

	public void deductQuantity(int quantity) {
		if (isQuantityLessThan(quantity)) {
			throw new IllegalArgumentException("차감할 재고 수량이 없습니다.");
		}
		this.quantity -= quantity;
	}

}