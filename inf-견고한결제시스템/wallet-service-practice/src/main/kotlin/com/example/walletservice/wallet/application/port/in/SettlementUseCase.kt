package com.example.walletservice.wallet.application.port.`in`

import com.example.walletservice.wallet.domain.PaymentEventMessage
import com.example.walletservice.wallet.domain.WalletEventMessage

interface SettlementUseCase {

  fun processSettlement(paymentEventMessage: PaymentEventMessage): WalletEventMessage
}