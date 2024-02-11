package com.fastcampus.helloecommerceservice.service.category;

import com.fastcampus.helloecommerceservice.controller.dto.product.CategoryProductDTO;
import com.fastcampus.helloecommerceservice.repository.category.CategoryProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CategoryProductService {
    private final CategoryProductRepository categoryProductRepository;

    public List<CategoryProductDTO> findAllBy(Long categoryId, Pageable pageable) {
        return this.categoryProductRepository.findAllByCategoryIdAndIsDeletedNot(categoryId, pageable);
    }
}
