package com.ys.cafekiosk.spring.domain.product;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {

	HANDMADE("제조 음료"),
	BOTTLE("병 음료"),
	BAKERY("베이커리");

	private final String text;

	public static boolean containsStockType(ProductType type) {
		return List.of(BOTTLE, BAKERY).contains(type);
	}
}
