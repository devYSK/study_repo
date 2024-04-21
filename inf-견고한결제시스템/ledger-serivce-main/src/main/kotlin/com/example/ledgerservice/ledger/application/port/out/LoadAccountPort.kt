package com.example.ledgerservice.ledger.application.port.out

import com.example.ledgerservice.ledger.domain.DoubleAccountsForLedger
import com.example.ledgerservice.ledger.domain.FinanceType

interface LoadAccountPort {

  fun getDoubleAccountsForLedger(financeType: FinanceType): DoubleAccountsForLedger
}