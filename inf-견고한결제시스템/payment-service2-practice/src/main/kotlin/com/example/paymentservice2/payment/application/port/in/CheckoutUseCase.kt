package com.example.paymentservice2.payment.application.port.`in`

import com.example.paymentservice2.payment.domain.CheckoutResult
import reactor.core.publisher.Mono

interface CheckoutUseCase {

  fun checkout(command: CheckoutCommand): Mono<CheckoutResult>
}