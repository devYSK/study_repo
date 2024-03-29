package com.fastcamedu.hellographql;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDetailView {
    private Long productId;
    private String productName;
    private String imageUrl;
    private int stockQuantity;
    private BigDecimal price;
    private boolean isExposed;
    private Long vendorId;
    private String vendorName;
    private OffsetDateTime createdAt;
    private String createdBy;
}
