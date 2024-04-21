package com.example.paymentservice2.payment.domain

data class Product (
  val id: Long,
  val amount: Long,
  val quantity: Int,
  val name: String,
  val sellerId: Long
)