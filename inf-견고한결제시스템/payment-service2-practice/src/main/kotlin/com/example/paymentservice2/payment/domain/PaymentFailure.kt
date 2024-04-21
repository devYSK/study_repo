package com.example.paymentservice2.payment.domain

data class PaymentFailure (
  val errorCode: String,
  val message: String
)