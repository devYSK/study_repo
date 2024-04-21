package com.example.ledgerservice.ledger.adapter.out.persistence.entity

import com.example.ledgerservice.common.IdempotencyCreator
import com.example.ledgerservice.ledger.domain.LedgerTransaction
import org.springframework.stereotype.Component

@Component
class JpaLedgerTransactionMapper {

  fun mapToJpaEntity(ledgerTransaction: LedgerTransaction): JpaLedgerTransactionEntity {
    return JpaLedgerTransactionEntity(
      description = "LedgerService record transaction",
      referenceId = ledgerTransaction.referenceId,
      referenceType = ledgerTransaction.referenceType,
      orderId = ledgerTransaction.orderId,
      idempotencyKey = IdempotencyCreator.createIdempotencyKey(ledgerTransaction)
    )
  }
}