package com.example.PaymentService.pg;

public interface CreditCardPaymentAdapter {
    Long processCreditCardPayment(Long amountKRW, String creditCardNumber);
}
