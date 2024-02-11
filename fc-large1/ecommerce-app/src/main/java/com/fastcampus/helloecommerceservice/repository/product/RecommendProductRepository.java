package com.fastcampus.helloecommerceservice.repository.product;

import com.fastcampus.helloecommerceservice.domain.product.Product;
import com.fastcampus.helloecommerceservice.domain.product.RecommendProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecommendProductRepository extends JpaRepository<RecommendProduct, Long> {

    @Query("SELECT p " +
            "FROM RecommendProduct rp " +
            "INNER JOIN Product p ON rp.productId = p.id " +
            "WHERE rp.isDeleted = false")
    List<Product> findAllByJPQL();
}
