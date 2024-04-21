package com.ys.paymentservice.domain

data class CheckoutResult (
  val amount: Long,
  val orderId: String,
  val orderName: String
)