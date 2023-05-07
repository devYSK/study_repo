package com.ys.cafekiosk.spring.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ys.cafekiosk.spring.api.service.product.ProductService;
import com.ys.cafekiosk.spring.api.service.product.response.ProductResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping("/api/v1/products/selling")
	public List<ProductResponse> getSellingProducts() {
		return productService.getSellingProducts();
	}
}
