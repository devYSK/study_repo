package com.fastcampus.helloecommerceservice.controller.dto.cart;

import lombok.Data;

@Data
public class AddCartDTO {
    private Long customerId;
    private Long productId;
}
