package com.example.paymentservice2.payment.application.port.out

import com.example.paymentservice2.payment.domain.PendingPaymentEvent
import reactor.core.publisher.Flux

interface LoadPendingPaymentPort {

  fun getPendingPayments(): Flux<PendingPaymentEvent>
}