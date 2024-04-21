package com.example.paymentservice2.payment.application.port.out

import com.example.paymentservice2.payment.domain.PaymentEventMessage
import reactor.core.publisher.Flux

interface LoadPendingPaymentEventMessagePort {

  fun getPendingPaymentEventMessage(): Flux<PaymentEventMessage>
}