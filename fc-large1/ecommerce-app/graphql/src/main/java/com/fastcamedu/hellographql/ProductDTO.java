package com.fastcamedu.hellographql;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {
    private final Long productId;
    private final String productName;
    private final String imageUrl;
    private final int stockQuantity;
    private final BigDecimal price;
    private final Long vendorId;
    private final String vendorName;
    private final OffsetDateTime createdAt;
    private final String createdBy;

    public static ProductDTO of(Product product) {
        return ProductDTO.builder()
            .productId(product.getProductId())
            .productName(product.getProductName())
            .imageUrl(product.getImageUrl())
            .stockQuantity(product.getStockQuantity())
            .price(product.getPrice())
            .vendorId(product.getVendorId())
            .vendorName(product.getProductName())
            .createdAt(product.getCreatedAt())
            .createdBy(product.getCreatedBy())
            .build();
    }

}