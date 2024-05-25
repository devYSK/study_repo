package com.example.PaymentService.pg;

import org.springframework.stereotype.Service;

@Service
public class EasyCreditCardPaymentAdapter implements CreditCardPaymentAdapter {

    @Override
    public Long processCreditCardPayment(Long amountKRW, String creditCardNumber) {
        // actual process with external system

        return Math.round(Math.random() * 100000000);
    }
}
