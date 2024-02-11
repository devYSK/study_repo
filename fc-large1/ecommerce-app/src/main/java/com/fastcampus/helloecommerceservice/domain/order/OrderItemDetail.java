package com.fastcampus.helloecommerceservice.domain.order;

import com.fastcampus.helloecommerceservice.enums.DeliveryType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemDetail {
    private Long orderId;
    private Long orderItemId;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productImageUrl;
    private int quantity;
    private DeliveryType deliveryType;
}
