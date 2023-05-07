package com.ys.cafekiosk.spring.api.service.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ys.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.ys.cafekiosk.spring.domain.product.Product;
import com.ys.cafekiosk.spring.domain.product.ProductRepository;
import com.ys.cafekiosk.spring.domain.product.ProductSellingStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public List<ProductResponse> getSellingProducts() {
		List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

		return products.stream()
			.map(ProductResponse::of)
			.toList();
	}
}
