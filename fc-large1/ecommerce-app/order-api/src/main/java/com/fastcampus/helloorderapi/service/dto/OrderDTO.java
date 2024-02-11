package com.fastcampus.helloorderapi.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data(staticConstructor = "of")
public class OrderDTO {
    private final Long orderId;
    private final BigDecimal amount;
    private final Long customerId;
    private final OffsetDateTime orderDate;
}
