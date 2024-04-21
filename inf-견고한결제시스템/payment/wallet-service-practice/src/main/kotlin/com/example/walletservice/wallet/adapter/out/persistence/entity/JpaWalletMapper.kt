package com.example.walletservice.wallet.adapter.out.persistence.entity

import com.example.walletservice.wallet.domain.Wallet
import org.springframework.stereotype.Component

@Component
class JpaWalletMapper {

  fun mapToDomainEntity(jpaWalletEntity: JpaWalletEntity): Wallet {
    return Wallet(
      id = jpaWalletEntity.id!!,
      userId = jpaWalletEntity.userId,
      version = jpaWalletEntity.version,
      balance = jpaWalletEntity.balance
    )
  }

  fun mapToJpaEntity(wallet: Wallet): JpaWalletEntity {
    return JpaWalletEntity(
      id = wallet.id,
      userId = wallet.userId,
      balance = wallet.balance,
      version = wallet.version
    )
  }
}