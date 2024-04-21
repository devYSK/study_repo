package com.example.paymentservice2.payment.adapter.`in`.web.request

data class TossPaymentConfirmRequest (
  val paymentKey: String,
  val orderId: String,
  val amount: String
)