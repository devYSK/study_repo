package com.example.ledgerservice.ledger.adapter.out.persistence

import com.example.ledgerservice.common.PersistenceAdapter
import com.example.ledgerservice.ledger.adapter.out.persistence.repository.AccountRepository
import com.example.ledgerservice.ledger.application.port.out.LoadAccountPort
import com.example.ledgerservice.ledger.domain.DoubleAccountsForLedger
import com.example.ledgerservice.ledger.domain.FinanceType

@PersistenceAdapter
class AccountPersistentAdapter (
  private val accountRepository: AccountRepository
) : LoadAccountPort {

  override fun getDoubleAccountsForLedger(financeType: FinanceType): DoubleAccountsForLedger {
    return accountRepository.getDoubleAccountsForLedger(financeType)
  }
}