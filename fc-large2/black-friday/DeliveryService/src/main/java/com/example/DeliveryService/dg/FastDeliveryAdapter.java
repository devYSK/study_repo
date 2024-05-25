package com.example.DeliveryService.dg;

import org.springframework.stereotype.Service;

@Service
public class FastDeliveryAdapter implements DeliveryAdapter {
    @Override
    public Long processDelivery(String productName, Long productCount, String address) {
        // delivery process

        return 11111111L;
    }
}
