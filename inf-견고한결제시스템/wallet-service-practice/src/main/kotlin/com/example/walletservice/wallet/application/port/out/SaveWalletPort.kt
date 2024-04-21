package com.example.walletservice.wallet.application.port.out

import com.example.walletservice.wallet.domain.Wallet

interface SaveWalletPort {

  fun save(wallets: List<Wallet>)
}