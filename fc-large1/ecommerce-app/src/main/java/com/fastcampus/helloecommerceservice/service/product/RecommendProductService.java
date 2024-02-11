package com.fastcampus.helloecommerceservice.service.product;

import com.fastcampus.helloecommerceservice.controller.dto.home.RecommendProductDTO;
import com.fastcampus.helloecommerceservice.domain.product.Product;
import com.fastcampus.helloecommerceservice.repository.product.RecommendProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendProductService {

    private final RecommendProductRepository recommendProductRepository;

    public List<RecommendProductDTO> recommend() {
        List<Product> recommendProducts = recommendProductRepository.findAllByJPQL();
        List<RecommendProductDTO> recommendProductDTOS = recommendProducts.stream().map((recommendProduct -> {
            RecommendProductDTO recommendProductDTO = RecommendProductDTO.of(
                    recommendProduct.getId(),
                    recommendProduct.getProductName(),
                    recommendProduct.getPrice(),
                    recommendProduct.getImageUrl()
            );
            return recommendProductDTO;
        })).collect(Collectors.toList());
        log.debug("추천 상품 목록 : {}", recommendProductDTOS);
        return recommendProductDTOS;
    }
}
