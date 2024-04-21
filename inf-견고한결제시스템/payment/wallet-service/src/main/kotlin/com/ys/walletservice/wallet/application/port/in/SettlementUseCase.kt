package com.ys.walletservice.wallet.application.port.`in`

import com.ys.walletservice.wallet.domain.PaymentEventMessage
import com.ys.walletservice.wallet.domain.WalletEventMessage

interface SettlementUseCase {

  fun processSettlement(paymentEventMessage: PaymentEventMessage): WalletEventMessage
}