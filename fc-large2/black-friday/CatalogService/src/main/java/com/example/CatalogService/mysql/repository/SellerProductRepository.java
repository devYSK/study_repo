package com.example.CatalogService.mysql.repository;

import com.example.CatalogService.mysql.entity.SellerProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerProductRepository extends JpaRepository<SellerProduct, Long> {
    List<SellerProduct> findBySellerId(Long sellerId);
}
