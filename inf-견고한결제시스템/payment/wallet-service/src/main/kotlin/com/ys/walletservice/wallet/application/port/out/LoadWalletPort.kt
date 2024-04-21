package com.ys.walletservice.wallet.application.port.out

import com.ys.walletservice.wallet.domain.Wallet

interface LoadWalletPort {

  fun getWallets(sellerIds: Set<Long>): Set<Wallet>
}