package com.example.paymentservice2.payment.application.port.`in`

data class PaymentConfirmCommand (
  val paymentKey: String,
  val orderId: String,
  val amount: Long
)