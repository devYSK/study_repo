package com.example.ledgerservice.ledger.adapter.out.persistence.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "payment_orders")
class JpaPaymentOrderEntity (
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  val amount: BigDecimal,

  @Column(name = "order_id")
  val orderId: String
)