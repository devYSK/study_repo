package com.example.ledgerservice.ledger.adapter.out.persistence

import com.example.ledgerservice.common.PersistenceAdapter
import com.example.ledgerservice.ledger.adapter.out.persistence.repository.LedgerEntryRepository
import com.example.ledgerservice.ledger.adapter.out.persistence.repository.LedgerTransactionRepository
import com.example.ledgerservice.ledger.application.port.out.DuplicateMessageFilterPort
import com.example.ledgerservice.ledger.application.port.out.SaveDoubleLedgerEntryPort
import com.example.ledgerservice.ledger.domain.DoubleLedgerEntry
import com.example.ledgerservice.ledger.domain.PaymentEventMessage

@PersistenceAdapter
class LedgerPersistenceAdapter (
  private val ledgerTransactionRepository: LedgerTransactionRepository,
  private val ledgerEntryRepository: LedgerEntryRepository
) : DuplicateMessageFilterPort, SaveDoubleLedgerEntryPort {

  override fun isAlreadyProcess(paymentEventMessage: PaymentEventMessage): Boolean {
    return ledgerTransactionRepository.isExist(paymentEventMessage)
  }

  override fun save(doubleLedgerEntryList: List<DoubleLedgerEntry>) {
    ledgerEntryRepository.save(doubleLedgerEntryList)
  }
}