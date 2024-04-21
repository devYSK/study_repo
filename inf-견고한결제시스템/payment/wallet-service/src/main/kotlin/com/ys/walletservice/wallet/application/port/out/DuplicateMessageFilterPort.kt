package com.ys.walletservice.wallet.application.port.out

import com.ys.walletservice.wallet.domain.PaymentEventMessage

interface DuplicateMessageFilterPort {

  fun isAlreadyProcess(paymentEventMessage: PaymentEventMessage): Boolean
}