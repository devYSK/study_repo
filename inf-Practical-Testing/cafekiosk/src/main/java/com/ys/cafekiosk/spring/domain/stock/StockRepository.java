package com.ys.cafekiosk.spring.domain.stock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

	List<Stock> findAllByProductNumberIn(List<String> productNumbers);

}