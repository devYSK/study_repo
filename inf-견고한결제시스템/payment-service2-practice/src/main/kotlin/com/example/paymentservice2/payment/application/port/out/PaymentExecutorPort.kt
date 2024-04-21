package com.example.paymentservice2.payment.application.port.out

import com.example.paymentservice2.payment.application.port.`in`.PaymentConfirmCommand
import com.example.paymentservice2.payment.domain.PaymentExecutionResult
import reactor.core.publisher.Mono

interface PaymentExecutorPort {

  fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult>
}