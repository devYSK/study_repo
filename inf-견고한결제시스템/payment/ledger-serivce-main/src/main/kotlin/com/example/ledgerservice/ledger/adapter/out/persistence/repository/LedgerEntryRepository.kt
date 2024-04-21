package com.example.ledgerservice.ledger.adapter.out.persistence.repository

import com.example.ledgerservice.ledger.domain.DoubleLedgerEntry

interface LedgerEntryRepository {

  fun save(doubleLedgerEntryList: List<DoubleLedgerEntry>)
}