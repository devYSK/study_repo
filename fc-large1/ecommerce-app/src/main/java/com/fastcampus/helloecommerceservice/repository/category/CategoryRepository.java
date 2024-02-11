package com.fastcampus.helloecommerceservice.repository.category;

import com.fastcampus.helloecommerceservice.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
