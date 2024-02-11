package com.fastcampus.helloecommerceservice.service.product;

import com.fastcampus.helloecommerceservice.domain.product.Product;
import com.fastcampus.helloecommerceservice.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }
}
