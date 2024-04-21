package com.ys.paymentservice.domain.payment

data class PaymentFailure (
  val errorCode: String,
  val message: String
)