package com.fastcampus.ecommerce.admin.domain.order;

import com.fastcampus.ecommerce.admin.enums.OrderStatus;
import com.fastcampus.ecommerce.admin.enums.PayType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class OrderDetailView {
    private Long orderId;
    private Long customerId;
    private String customerName;
    private BigDecimal amount;
    private OrderStatus orderStatus;
    private PayType payType;
    private OffsetDateTime createdAt;
}
