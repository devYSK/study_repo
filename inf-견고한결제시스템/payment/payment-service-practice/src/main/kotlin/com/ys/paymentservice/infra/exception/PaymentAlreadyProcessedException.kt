package com.example.paymentservice2.payment.adapter.out.persistent.exception

import com.example.paymentservice2.payment.domain.PaymentStatus
import com.ys.paymentservice.domain.payment.PaymentStatus

class PaymentAlreadyProcessedException(
    val status: PaymentStatus,
    message: String
) : RuntimeException(message) {
}