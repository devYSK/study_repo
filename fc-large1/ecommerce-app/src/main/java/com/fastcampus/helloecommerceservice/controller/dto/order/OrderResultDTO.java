package com.fastcampus.helloecommerceservice.controller.dto.order;

import com.fastcampus.helloecommerceservice.domain.order.OrderResult;
import lombok.Data;

import java.math.BigDecimal;

@Data(staticConstructor = "of")
public class OrderResultDTO {
    private final Long orderId;
    private final BigDecimal totalAmount;
    private final String payType;
    private final String address;

    public static OrderResultDTO of(OrderResult orderResult) {
        return OrderResultDTO.of(orderResult.getOrderId(), orderResult.getTotalAmount(), orderResult.getPayType().name(), orderResult.getAddress());
    }
}
