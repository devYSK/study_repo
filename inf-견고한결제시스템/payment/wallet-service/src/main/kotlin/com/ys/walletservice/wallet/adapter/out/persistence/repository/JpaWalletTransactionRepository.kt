package com.ys.walletservice.wallet.adapter.out.persistence.repository

import com.ys.walletservice.wallet.adapter.out.persistence.entity.JpaWalletTransactionEntity
import com.ys.walletservice.wallet.adapter.out.persistence.entity.JpaWalletTransactionMapper
import com.ys.walletservice.wallet.domain.PaymentEventMessage
import com.ys.walletservice.wallet.domain.WalletTransaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class JpaWalletTransactionRepository (
  private val springDataJpaWalletTransactionRepository: SpringDataJpaWalletTransactionRepository,
  private val jpaWalletTransactionMapper: JpaWalletTransactionMapper
) : WalletTransactionRepository {

  override fun isExist(paymentEventMessage: PaymentEventMessage): Boolean {
    return springDataJpaWalletTransactionRepository.existsByOrderId(paymentEventMessage.orderId())
  }

  override fun save(walletTransactions: List<WalletTransaction>) {
    springDataJpaWalletTransactionRepository.saveAll(walletTransactions.map { jpaWalletTransactionMapper.mapToJpaEntity(it) })
  }
}

interface SpringDataJpaWalletTransactionRepository : JpaRepository<JpaWalletTransactionEntity, Long> {

  fun existsByOrderId(orderId: String): Boolean
}
