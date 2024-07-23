package com.example.paymentservice2.payment.adapter.out.web.toss

import com.example.paymentservice2.common.WebAdapter
import com.example.paymentservice2.payment.adapter.out.web.toss.executor.PaymentExecutor
import com.example.paymentservice2.payment.application.port.`in`.PaymentConfirmCommand
import com.example.paymentservice2.payment.application.port.out.PaymentExecutorPort
import com.example.paymentservice2.payment.domain.PaymentExecutionResult
import reactor.core.publisher.Mono

@WebAdapter
class PaymentExecutorWebAdapter (
  private val paymentExecutor: PaymentExecutor
) : PaymentExecutorPort {

  override fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult> {
    return paymentExecutor.execute(command)
  }
}