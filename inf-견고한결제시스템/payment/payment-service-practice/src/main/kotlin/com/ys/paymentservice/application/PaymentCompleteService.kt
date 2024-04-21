package com.ys.paymentservice.application

import com.ys.paymentservice.common.UseCase
import com.ys.paymentservice.domain.LedgerEventMessage
import com.ys.paymentservice.domain.payment.PaymentPersistentAdapter
import com.ys.paymentservice.domain.wallet.WalletEventMessage
import reactor.core.publisher.Mono

@UseCase
class PaymentCompleteService (
  private val loadPaymentPort: PaymentPersistentAdapter,
) {

  fun completePayment(walletEventMessage: WalletEventMessage): Mono<Void> {
    return loadPaymentPort.getPayment(walletEventMessage.orderId())
      .map { it.apply { confirmWalletUpdate() } }
      .map { it.apply { completeIfDone() } }
      .flatMap { loadPaymentPort.complete(it) }
  }

   fun completePayment(ledgerEventMessage: LedgerEventMessage): Mono<Void> {
    return loadPaymentPort.getPayment(ledgerEventMessage.orderId())
      .map { it.apply { confirmLedgerUpdate() } }
      .map { it.apply { completeIfDone() } }
      .flatMap { loadPaymentPort.complete(it) }
  }

}