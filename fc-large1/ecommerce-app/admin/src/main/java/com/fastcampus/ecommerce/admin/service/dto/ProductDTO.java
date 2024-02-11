package com.fastcampus.ecommerce.admin.service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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
}