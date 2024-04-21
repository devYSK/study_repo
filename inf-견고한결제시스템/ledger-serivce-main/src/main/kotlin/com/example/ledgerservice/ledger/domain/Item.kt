package com.example.ledgerservice.ledger.domain

open class Item (
  val id: Long,
  val amount: Long,
  val orderId: String,
  val type: ReferenceType
)

enum class ReferenceType {
  PAYMENT_ORDER
}