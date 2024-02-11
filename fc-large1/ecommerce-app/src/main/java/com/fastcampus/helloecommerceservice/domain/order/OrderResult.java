package com.fastcampus.helloecommerceservice.domain.order;

import com.fastcampus.helloecommerceservice.enums.PayType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data(staticConstructor = "of")
public class OrderResult {
    private final Long orderId;
    private final BigDecimal totalAmount;
    private final List<OrderItemDetail> orderItemDetails;
    private final PayType payType;
    private final String address;
}
