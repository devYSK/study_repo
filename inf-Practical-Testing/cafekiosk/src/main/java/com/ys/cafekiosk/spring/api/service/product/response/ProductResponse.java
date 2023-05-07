package com.ys.cafekiosk.spring.api.service.product.response;

import com.ys.cafekiosk.spring.domain.product.Product;
import com.ys.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.ys.cafekiosk.spring.domain.product.ProductType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

	private Long id;
	private String productNumber;
	private ProductType type;
	private ProductSellingStatus sellingStatus;
	private String name;
	private int price;

	@Builder
	private ProductResponse(Long id, String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name,
		int price) {
		this.id = id;
		this.productNumber = productNumber;
		this.type = type;
		this.sellingStatus = sellingStatus;
		this.name = name;
		this.price = price;
	}

	public static ProductResponse of(Product product) {
		return ProductResponse.builder()
			.id(product.getId())
			.productNumber(product.getProductNumber())
			.type(product.getType())
			.name(product.getName())
			.sellingStatus(product.getSellingStatus())
			.price(product.getPrice())
			.build();
	}
}
