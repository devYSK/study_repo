package com.ys.cafekiosk.spring.domain.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes);

	List<Product> findAllByProductNumberIn(List<String> productNumbers);

}
