package com.fastcampus.ecommerce.admin.service;

import com.fastcampus.ecommerce.admin.domain.product.ProductDetailView;
import com.fastcampus.ecommerce.admin.repository.ProductRepository;
import com.fastcampus.ecommerce.admin.service.dto.ProductDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductDTO> findAll() {
        List<ProductDetailView> productDetailViews = productRepository.findAllProductDetailView();
        return productDetailViews.stream().map(pv -> {
            return ProductDTO.builder()
                    .productId(pv.getProductId())
                    .productName(pv.getProductName())
                    .imageUrl(pv.getImageUrl())
                    .stockQuantity(pv.getStockQuantity())
                    .price(pv.getPrice())
                    .vendorId(pv.getVendorId())
                    .vendorName(pv.getVendorName())
                    .createdAt(pv.getCreatedAt())
                    .createdBy(pv.getCreatedBy())
                    .build();
        }).collect(Collectors.toList());
    }

    public Optional<ProductDetailView> getProductDetail(Long productId) {
        return productRepository.getProductDetail(productId);
    }
}
