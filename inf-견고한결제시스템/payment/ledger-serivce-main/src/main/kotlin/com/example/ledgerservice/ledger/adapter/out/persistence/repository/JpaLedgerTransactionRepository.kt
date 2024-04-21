package com.example.ledgerservice.ledger.adapter.out.persistence.repository

import com.example.ledgerservice.ledger.adapter.out.persistence.entity.JpaLedgerTransactionEntity
import com.example.ledgerservice.ledger.domain.PaymentEventMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class JpaLedgerTransactionRepository (
  private val springDataJpaLedgerTransactionRepository: SpringDataJpaLedgerTransactionRepository
) : LedgerTransactionRepository {

  override fun isExist(paymentEventMessage: PaymentEventMessage): Boolean {
    return springDataJpaLedgerTransactionRepository.existsByOrderId(paymentEventMessage.orderId())
  }
}

interface SpringDataJpaLedgerTransactionRepository : JpaRepository<JpaLedgerTransactionEntity, Long> {

  fun existsByOrderId(orderId: String): Boolean
}



