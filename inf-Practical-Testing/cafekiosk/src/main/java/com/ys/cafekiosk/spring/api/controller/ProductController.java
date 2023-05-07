package com.ys.cafekiosk.spring.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ys.cafekiosk.spring.api.ApiResponse;
import com.ys.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.ys.cafekiosk.spring.api.service.product.ProductService;
import com.ys.cafekiosk.spring.api.service.product.response.ProductResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping("/api/v1/products/new")
	public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
		return ApiResponse.ok(productService.createProduct(request.toServiceRequest()));
	}

	@GetMapping("/api/v1/products/selling")
	public ApiResponse<List<ProductResponse>> getSellingProducts() {
		return ApiResponse.ok(productService.getSellingProducts());
	}
}
