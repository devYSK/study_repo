package com.fastcampus.ecommerce.admin.repository;

import com.fastcampus.ecommerce.admin.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
