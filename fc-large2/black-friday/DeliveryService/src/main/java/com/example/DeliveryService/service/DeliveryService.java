package com.example.DeliveryService.service;

import com.example.DeliveryService.dg.DeliveryAdapter;
import com.example.DeliveryService.entity.Delivery;
import com.example.DeliveryService.entity.UserAddress;
import com.example.DeliveryService.enums.DeliveryStatus;
import com.example.DeliveryService.repository.DeliveryRepository;
import com.example.DeliveryService.repository.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {
    @Autowired
    UserAddressRepository userAddressRepository;
    
    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    DeliveryAdapter deliveryAdapter;

    public UserAddress addUserAddress(Long userId, String address, String alias) {
        var userAddress = new UserAddress(userId, address, alias);

        return userAddressRepository.save(userAddress);
    }

    public Delivery processDelivery(
            Long orderId,
            String productName,
            Long productCount,
            String address
    ) {
        var refCode = deliveryAdapter.processDelivery(productName, productCount, address);
        var delivery = new Delivery(orderId, productName, productCount, address, refCode, DeliveryStatus.REQUESTED);

        return deliveryRepository.save(delivery);
    }

    public Delivery getDelivery(Long deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow();
    }

    public UserAddress getAddress(Long addressId) {
        return userAddressRepository.findById(addressId).orElseThrow();
    }

    public UserAddress getUserAddress(Long userId) {
        return userAddressRepository.findByUserId(userId).stream().findFirst().orElseThrow();
    }
}
