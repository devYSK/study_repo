package com.example.ledgerservice.ledger.adapter.out.persistence.repository

import com.example.ledgerservice.ledger.domain.DoubleAccountsForLedger
import com.example.ledgerservice.ledger.domain.FinanceType

interface AccountRepository {
  fun getDoubleAccountsForLedger(financeType: FinanceType): DoubleAccountsForLedger
}