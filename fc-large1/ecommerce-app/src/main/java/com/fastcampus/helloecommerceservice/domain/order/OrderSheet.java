package com.fastcampus.helloecommerceservice.domain.order;

import com.fastcampus.helloecommerceservice.controller.dto.customer.CustomerDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data(staticConstructor = "of")
public class OrderSheet {
    private final Long orderId;
    private final OffsetDateTime orderDate;
    private final CustomerDTO customerDTO;
    private final String deliveryAddress;
    private final BigDecimal totalAmount;
    private final List<OrderItemDetail> orderItemDetails;
}
