package com.fastcampus.helloecommerceservice.service.search;

import com.fastcampus.helloecommerceservice.controller.dto.search.SearchResultDTO;
import com.fastcampus.helloecommerceservice.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductRepository productRepository;

    public List<SearchResultDTO> search(String keyword) {
        List<SearchResultDTO> searchResultDTOS = this.productRepository.search(keyword);
        if (CollectionUtils.isEmpty(searchResultDTOS)) {
            return Collections.EMPTY_LIST;
        }
        return searchResultDTOS;
    }
}
