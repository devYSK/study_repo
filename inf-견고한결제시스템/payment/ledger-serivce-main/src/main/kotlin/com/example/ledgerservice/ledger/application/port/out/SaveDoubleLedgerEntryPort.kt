package com.example.ledgerservice.ledger.application.port.out

import com.example.ledgerservice.ledger.domain.DoubleLedgerEntry

interface SaveDoubleLedgerEntryPort {

  fun save(doubleLedgerEntryList: List<DoubleLedgerEntry>)
}