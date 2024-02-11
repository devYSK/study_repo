package com.fastcampus.helloecommerceservice.controller.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryProductDTO {
    private Long categoryProductId;
    private Long categoryId;
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
}
