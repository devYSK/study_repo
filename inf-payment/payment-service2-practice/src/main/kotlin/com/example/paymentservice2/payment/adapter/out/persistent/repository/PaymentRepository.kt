package com.example.paymentservice2.payment.adapter.out.persistent.repository

import com.example.paymentservice2.payment.domain.PaymentEvent
import com.example.paymentservice2.payment.domain.PendingPaymentEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PaymentRepository {

  fun save(paymentEvent: PaymentEvent): Mono<Void>

  fun getPendingPayments(): Flux<PendingPaymentEvent>

  fun getPayment(orderId: String): Mono<PaymentEvent>

  fun complete(paymentEvent: PaymentEvent): Mono<Void>
}