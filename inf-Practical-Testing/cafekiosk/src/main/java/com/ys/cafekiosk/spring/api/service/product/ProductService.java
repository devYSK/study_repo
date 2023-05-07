package com.ys.cafekiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ys.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.ys.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import com.ys.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.ys.cafekiosk.spring.domain.product.Product;
import com.ys.cafekiosk.spring.domain.product.ProductRepository;
import com.ys.cafekiosk.spring.domain.product.ProductSellingStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional
	public ProductResponse createProduct(ProductCreateServiceRequest request) {
		String nextProductNumber = createNextProductNumber();

		Product product = request.toEntity(nextProductNumber);
		Product savedProduct = productRepository.save(product);

		return ProductResponse.of(savedProduct);
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> getSellingProducts() {
		List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

		return products.stream()
			.map(ProductResponse::of)
			.collect(Collectors.toList());
	}

	private String createNextProductNumber() {
		String latestProductNumber = productRepository.findLatestProductNumber();
		if (latestProductNumber == null) {
			return "001";
		}

		int latestProductNumberInt = Integer.parseInt(latestProductNumber);
		int nextProductNumberInt = latestProductNumberInt + 1;

		return String.format("%03d", nextProductNumberInt);
	}

}
