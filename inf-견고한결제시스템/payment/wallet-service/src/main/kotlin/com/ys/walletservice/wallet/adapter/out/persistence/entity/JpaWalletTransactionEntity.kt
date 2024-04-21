package com.ys.walletservice.wallet.adapter.out.persistence.entity

import com.ys.walletservice.wallet.domain.TransactionType
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "wallet_transactions")
class JpaWalletTransactionEntity (

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "wallet_id")
  val walletId: Long,

  @Column
  val amount: BigDecimal,

  @Enumerated(value = EnumType.STRING)
  val type: TransactionType,

  @Column(name = "order_id")
  val orderId: String,

  @Column(name = "reference_type")
  val referenceType: String,

  @Column(name = "reference_id")
  val referenceId: Long,

  @Column(name = "idempotency_key")
  val idempotencyKey: String
)