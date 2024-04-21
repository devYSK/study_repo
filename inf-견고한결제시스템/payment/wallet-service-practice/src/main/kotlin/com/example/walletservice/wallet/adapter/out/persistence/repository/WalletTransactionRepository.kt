package com.example.walletservice.wallet.adapter.out.persistence.repository

import com.example.walletservice.wallet.domain.PaymentEventMessage
import com.example.walletservice.wallet.domain.WalletTransaction

interface WalletTransactionRepository {

  fun isExist(paymentEventMessage: PaymentEventMessage): Boolean

  fun save(walletTransactions: List<WalletTransaction>)
}