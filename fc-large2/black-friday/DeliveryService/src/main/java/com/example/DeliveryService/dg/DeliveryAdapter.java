package com.example.DeliveryService.dg;

public interface DeliveryAdapter {
    Long processDelivery(String productName, Long productCount, String address);
}
