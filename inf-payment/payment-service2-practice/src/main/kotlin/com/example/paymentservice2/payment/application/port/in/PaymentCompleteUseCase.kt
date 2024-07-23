package com.example.paymentservice2.payment.application.port.`in`

import com.example.paymentservice2.payment.domain.LedgerEventMessage
import com.example.paymentservice2.payment.domain.WalletEventMessage
import reactor.core.publisher.Mono

interface PaymentCompleteUseCase {

  fun completePayment(walletEventMessage: WalletEventMessage): Mono<Void>

  fun completePayment(ledgerEventMessage: LedgerEventMessage): Mono<Void>
}