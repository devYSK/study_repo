package com.example.DeliveryService.repository;

import com.example.DeliveryService.entity.Delivery;
import com.example.DeliveryService.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findAllByOrderId(Long orderId);

    List<Delivery> findAllByStatus(DeliveryStatus status);
}
