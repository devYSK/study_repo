package com.example.ledgerservice.ledger.adapter.out.persistence.repository

import com.example.ledgerservice.ledger.adapter.out.persistence.entity.JpaAccountEntity
import com.example.ledgerservice.ledger.adapter.out.persistence.entity.JpaAccountMapper
import com.example.ledgerservice.ledger.domain.DoubleAccountsForLedger
import com.example.ledgerservice.ledger.domain.FinanceType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class JpaAccountRepository (
  private val springDataJpaAccountRepository: SpringDataJpaAccountRepository,
  private val jpaAccountMapper: JpaAccountMapper
): AccountRepository {

  override fun getDoubleAccountsForLedger(financeType: FinanceType): DoubleAccountsForLedger {
    return when (financeType) {
      FinanceType.PAYMENT_ORDER -> {
        val to = springDataJpaAccountRepository.findByName(REVENUE_ACCOUNT_NAME)
        val from = springDataJpaAccountRepository.findByName(ITEM_BUYER_NAME)

        DoubleAccountsForLedger(
          to = jpaAccountMapper.mapToDomainEntity(to),
          from = jpaAccountMapper.mapToDomainEntity(from)
        )
      }
    }
  }

  companion object {
    const val REVENUE_ACCOUNT_NAME = "REVENUE"
    const val ITEM_BUYER_NAME = "ITEM_BUYER"
  }
}

interface SpringDataJpaAccountRepository : JpaRepository<JpaAccountEntity, Long>{

  fun findByName(name: String): JpaAccountEntity
}
