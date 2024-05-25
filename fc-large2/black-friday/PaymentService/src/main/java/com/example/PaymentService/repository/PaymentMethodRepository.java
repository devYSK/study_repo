package com.example.PaymentService.repository;

import com.example.PaymentService.entity.Payment;
import com.example.PaymentService.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    List<PaymentMethod> findByUserId(Long userId);
}
