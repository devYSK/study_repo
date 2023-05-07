package com.ys.cafekiosk.spring.domain.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes);

	List<Product> findAllByProductNumberIn(List<String> productNumbers);

	@Query(value = "select product_number from product order by id desc limit 1", nativeQuery = true)
	String findLatestProductNumber();

}
