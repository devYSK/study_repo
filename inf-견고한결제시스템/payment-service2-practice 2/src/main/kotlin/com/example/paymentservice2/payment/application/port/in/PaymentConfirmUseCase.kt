package com.example.paymentservice2.payment.application.port.`in`

import com.example.paymentservice2.payment.domain.PaymentConfirmationResult
import reactor.core.publisher.Mono

interface PaymentConfirmUseCase {

  fun confirm(command: PaymentConfirmCommand): Mono<PaymentConfirmationResult>
}