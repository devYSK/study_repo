package com.example.walletservice.wallet.adapter.out.persistence.repository

import com.example.walletservice.wallet.domain.Wallet

interface WalletRepository {

  fun getWallets(sellerIds: Set<Long>): Set<Wallet>

  fun save(wallets: List<Wallet>)
}