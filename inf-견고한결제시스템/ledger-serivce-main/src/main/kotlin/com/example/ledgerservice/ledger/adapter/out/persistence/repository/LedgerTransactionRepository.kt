package com.example.ledgerservice.ledger.adapter.out.persistence.repository

import com.example.ledgerservice.ledger.domain.PaymentEventMessage

interface LedgerTransactionRepository {

  fun isExist(paymentEventMessage: PaymentEventMessage): Boolean
}