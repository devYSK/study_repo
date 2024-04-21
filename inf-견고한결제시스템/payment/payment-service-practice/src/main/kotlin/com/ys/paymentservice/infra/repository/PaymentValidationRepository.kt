package com.ys.paymentservice.infra.repository

import reactor.core.publisher.Mono

interface PaymentValidationRepository {

  fun isValid(orderId: String, amount: Long): Mono<Boolean>
}