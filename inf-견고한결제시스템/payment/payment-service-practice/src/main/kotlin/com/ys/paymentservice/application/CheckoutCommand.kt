package com.ys.paymentservice.application

data class CheckoutCommand (
  val cartId: Long,
  val buyerId: Long,
  val productIds: List<Long>,
  val idempotencyKey: String
)