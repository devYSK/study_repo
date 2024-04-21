package com.example.walletservice.wallet.adapter.out.persistence.repository

import com.example.walletservice.wallet.adapter.out.persistence.entity.JpaWalletTransactionEntity
import com.example.walletservice.wallet.adapter.out.persistence.entity.JpaWalletTransactionMapper
import com.example.walletservice.wallet.domain.PaymentEventMessage
import com.example.walletservice.wallet.domain.WalletTransaction
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
