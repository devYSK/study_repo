package com.fastcampus.helloecommerceservice.controller.dto.home;

import lombok.Data;

import java.math.BigDecimal;

@Data(staticConstructor = "of")
public class RecommendProductDTO {
    private final Long productId;
    private final String productName;
    private final BigDecimal productPrice;
    private final String productImageUrl;
}
