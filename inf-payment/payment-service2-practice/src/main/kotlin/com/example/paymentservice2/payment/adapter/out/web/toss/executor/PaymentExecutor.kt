package com.example.paymentservice2.payment.adapter.out.web.toss.executor

import com.example.paymentservice2.payment.application.port.`in`.PaymentConfirmCommand
import com.example.paymentservice2.payment.domain.PaymentExecutionResult
import reactor.core.publisher.Mono

interface PaymentExecutor {

  fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult>
}