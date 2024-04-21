package com.example.paymentservice2.payment.adapter.out.persistent.exception

import com.example.paymentservice2.payment.domain.PaymentStatus

class PaymentAlreadyProcessedException(
  val status: PaymentStatus,
  message: String
) : RuntimeException(message) {
}