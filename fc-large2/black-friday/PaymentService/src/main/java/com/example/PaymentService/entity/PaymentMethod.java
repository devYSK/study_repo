package com.example.PaymentService.entity;

import com.example.PaymentService.enums.PaymentMethodType;
import jakarta.persistence.*;

@Entity
@Table(indexes = {@Index(name = "idx_userId", columnList = "userId")})
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long userId;
    public PaymentMethodType paymentMethodType;
    public String creditCardNumber;

    public PaymentMethod() {
    }

    public PaymentMethod(Long userId, PaymentMethodType paymentMethodType, String creditCardNumber) {
        this.userId = userId;
        this.paymentMethodType = paymentMethodType;
        this.creditCardNumber = creditCardNumber;
    }
}
