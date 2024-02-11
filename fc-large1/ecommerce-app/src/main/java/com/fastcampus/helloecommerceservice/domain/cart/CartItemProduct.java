package com.fastcampus.helloecommerceservice.domain.cart;

import com.fastcampus.helloecommerceservice.enums.DeliveryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemProduct {
    private Long cartId;
    private Long cartItemId;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productImageUrl;
    private int quantity;
    private DeliveryType deliveryType;
}
