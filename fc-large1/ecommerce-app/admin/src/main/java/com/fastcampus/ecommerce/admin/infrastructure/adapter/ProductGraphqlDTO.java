package com.fastcampus.ecommerce.admin.infrastructure.adapter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
class ProductGraphqlDTO {
    private Long productId;
    private BigDecimal price;
    private OffsetDateTime createdAt;
}