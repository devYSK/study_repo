package com.example.paymentservice2.payment.adapter.out.persistent.repository

import reactor.core.publisher.Mono

interface PaymentValidationRepository {

  fun isValid(orderId: String, amount: Long): Mono<Boolean>
}