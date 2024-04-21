package com.ys.walletservice.wallet.adapter.out.persistence.repository

import com.ys.walletservice.wallet.domain.Wallet

interface WalletRepository {

  fun getWallets(sellerIds: Set<Long>): Set<Wallet>

  fun save(wallets: List<Wallet>)
}