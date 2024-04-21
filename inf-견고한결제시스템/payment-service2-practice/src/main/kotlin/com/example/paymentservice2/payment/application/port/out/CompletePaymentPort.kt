package com.example.paymentservice2.payment.application.port.out

import com.example.paymentservice2.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

interface CompletePaymentPort {

  fun complete(paymentEvent: PaymentEvent): Mono<Void>
}