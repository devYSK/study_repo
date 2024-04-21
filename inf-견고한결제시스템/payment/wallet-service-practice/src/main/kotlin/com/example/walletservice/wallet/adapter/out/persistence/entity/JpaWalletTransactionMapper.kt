package com.example.walletservice.wallet.adapter.out.persistence.entity

import com.example.walletservice.common.IdempotencyCreator
import com.example.walletservice.wallet.domain.WalletTransaction
import org.springframework.stereotype.Component

@Component
class JpaWalletTransactionMapper {

  fun mapToJpaEntity(walletTransaction: WalletTransaction): JpaWalletTransactionEntity {
    return JpaWalletTransactionEntity(
      walletId = walletTransaction.walletId,
      amount = walletTransaction.amount.toBigDecimal(),
      type = walletTransaction.type,
      referenceType = walletTransaction.referenceType.name,
      referenceId = walletTransaction.referenceId,
      orderId = walletTransaction.orderId,
      idempotencyKey = IdempotencyCreator.create(walletTransaction)
    )
  }
}