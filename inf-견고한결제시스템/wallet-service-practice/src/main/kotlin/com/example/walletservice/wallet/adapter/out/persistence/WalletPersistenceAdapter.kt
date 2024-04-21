package com.example.walletservice.wallet.adapter.out.persistence

import com.example.walletservice.common.PersistenceAdapter
import com.example.walletservice.wallet.adapter.out.persistence.repository.WalletRepository
import com.example.walletservice.wallet.adapter.out.persistence.repository.WalletTransactionRepository
import com.example.walletservice.wallet.application.port.out.DuplicateMessageFilterPort
import com.example.walletservice.wallet.application.port.out.LoadWalletPort
import com.example.walletservice.wallet.application.port.out.SaveWalletPort
import com.example.walletservice.wallet.domain.PaymentEventMessage
import com.example.walletservice.wallet.domain.Wallet

@PersistenceAdapter
class WalletPersistenceAdapter (
  private val walletTransactionRepository: WalletTransactionRepository,
  private val walletRepository: WalletRepository
) : DuplicateMessageFilterPort, LoadWalletPort, SaveWalletPort {

  override fun isAlreadyProcess(paymentEventMessage: PaymentEventMessage): Boolean {
    return walletTransactionRepository.isExist(paymentEventMessage)
  }

  override fun getWallets(sellerIds: Set<Long>): Set<Wallet> {
    return walletRepository.getWallets(sellerIds)
  }

  override fun save(wallets: List<Wallet>) {
    return walletRepository.save(wallets)
  }
}