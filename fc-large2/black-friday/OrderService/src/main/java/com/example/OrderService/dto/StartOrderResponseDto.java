package com.example.OrderService.dto;

import java.util.Map;

public class StartOrderResponseDto {
    public Long orderId;
    public Map<String, Object> paymentMethod;
    public Map<String, Object> address;
}
