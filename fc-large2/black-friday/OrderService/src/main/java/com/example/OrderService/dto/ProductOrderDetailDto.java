package com.example.OrderService.dto;

import com.example.OrderService.enums.OrderStatus;

public class ProductOrderDetailDto {
    public Long id;
    public Long userId;
    public Long productId;
    public Long paymentId;
    public Long deliveryId;
    public OrderStatus orderStatus;

    public String paymentStatus;
    public String deliveryStatus;

    public ProductOrderDetailDto(Long id, Long userId, Long productId, Long paymentId, Long deliveryId, OrderStatus orderStatus, String paymentStatus, String deliveryStatus) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.paymentId = paymentId;
        this.deliveryId = deliveryId;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.deliveryStatus = deliveryStatus;
    }
}
