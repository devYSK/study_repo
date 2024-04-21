package com.example.walletservice.wallet.application.port.out

import com.example.walletservice.wallet.domain.Wallet

interface LoadWalletPort {

  fun getWallets(sellerIds: Set<Long>): Set<Wallet>
}