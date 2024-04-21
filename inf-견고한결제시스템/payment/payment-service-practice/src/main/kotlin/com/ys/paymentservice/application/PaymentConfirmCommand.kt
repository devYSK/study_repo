package com.ys.paymentservice.application

data class PaymentConfirmCommand (
  val paymentKey: String,
  val orderId: String,
  val amount: Long
)