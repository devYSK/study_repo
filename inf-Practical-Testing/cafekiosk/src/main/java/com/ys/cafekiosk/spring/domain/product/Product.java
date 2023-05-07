package com.ys.cafekiosk.spring.domain.product;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productNumber;

	@Enumerated(EnumType.STRING)
	private ProductType type;

	@Enumerated(EnumType.STRING)
	private ProductSellingStatus sellingStatus;

	private String name;

	private int price;

	@Builder
	private Product(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
		this.productNumber = productNumber;
		this.type = type;
		this.sellingStatus = sellingStatus;
		this.name = name;
		this.price = price;
	}

}
