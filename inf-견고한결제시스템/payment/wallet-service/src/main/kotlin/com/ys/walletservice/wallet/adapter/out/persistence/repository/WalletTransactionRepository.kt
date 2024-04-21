package com.ys.walletservice.wallet.adapter.out.persistence.repository

import com.ys.walletservice.wallet.domain.PaymentEventMessage
import com.ys.walletservice.wallet.domain.WalletTransaction

interface WalletTransactionRepository {

  fun isExist(paymentEventMessage: PaymentEventMessage): Boolean

  fun save(walletTransactions: List<WalletTransaction>)
}