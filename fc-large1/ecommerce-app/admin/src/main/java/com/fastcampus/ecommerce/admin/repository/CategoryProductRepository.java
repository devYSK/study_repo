package com.fastcampus.ecommerce.admin.repository;

import com.fastcampus.ecommerce.admin.domain.category.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Long> {
}
