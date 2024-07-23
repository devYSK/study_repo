package com.example.paymentservice2.payment.test

import com.example.paymentservice2.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

interface PaymentDatabaseHelper {

  fun getPayments(orderId: String): PaymentEvent?

  fun clean(): Mono<Void>
}