package com.example.walletservice.wallet.application.port.out

import com.example.walletservice.wallet.domain.PaymentEventMessage

interface DuplicateMessageFilterPort {

  fun isAlreadyProcess(paymentEventMessage: PaymentEventMessage): Boolean
}