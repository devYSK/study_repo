package com.fastcampus.helloecommerceservice.controller.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SearchResultDTO {
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
}
