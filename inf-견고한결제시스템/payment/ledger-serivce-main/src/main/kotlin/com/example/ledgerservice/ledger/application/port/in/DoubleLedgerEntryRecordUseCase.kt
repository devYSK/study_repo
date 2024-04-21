package com.example.ledgerservice.ledger.application.port.`in`

import com.example.ledgerservice.ledger.domain.LedgerEventMessage
import com.example.ledgerservice.ledger.domain.PaymentEventMessage

interface DoubleLedgerEntryRecordUseCase {

  fun recordDoubleLedgerEntry(message: PaymentEventMessage): LedgerEventMessage
}