package com.fastcampus.helloecommerceservice.service.category;

import com.fastcampus.helloecommerceservice.domain.category.Category;
import com.fastcampus.helloecommerceservice.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public String getName(Long categoryId) {
        Optional<Category> optionalCategory = this.categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            return Strings.EMPTY;
        }
        return optionalCategory.get().getCategoryName();
    }
}
