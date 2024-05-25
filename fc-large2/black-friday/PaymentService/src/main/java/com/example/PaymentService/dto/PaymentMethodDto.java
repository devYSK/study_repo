package com.example.PaymentService.dto;

import com.example.PaymentService.enums.PaymentMethodType;

public class PaymentMethodDto {
    public Long userId;
    public PaymentMethodType paymentMethodType;
    public String creditCardNumber;
}
