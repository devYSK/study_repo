package com.ys.paymentservice.application

import com.ys.paymentservice.common.UseCase
import com.ys.paymentservice.domain.payment.PaymentConfirmationResult
import com.ys.paymentservice.domain.payment.PaymentPersistentAdapter
import com.ys.paymentservice.infra.TossPaymentExecutor
import reactor.core.publisher.Mono

@UseCase
class PaymentConfirmService (
  private val paymentStatusUpdatePort: PaymentPersistentAdapter,
  private val paymentValidationPort: PaymentPersistentAdapter,
  private val paymentExecutorPort: TossPaymentExecutor,
  private val paymentErrorHandler: PaymentErrorHandler
)  {

  fun confirm(command: PaymentConfirmCommand): Mono<PaymentConfirmationResult> {
    return paymentStatusUpdatePort.updatePaymentStatusToExecuting(command.orderId, command.paymentKey)
      .filterWhen { paymentValidationPort.isValid(command.orderId, command.amount) }
      .flatMap { paymentExecutorPort.execute(command) }
      .flatMap {
        paymentStatusUpdatePort.updatePaymentStatus(
          command = PaymentStatusUpdateCommand(
            paymentKey = it.paymentKey,
            orderId = it.orderId,
            status = it.paymentStatus(),
            extraDetails = it.extraDetails,
            failure = it.failure
          )
        ).thenReturn(it)
      }
      .map { PaymentConfirmationResult(status = it.paymentStatus(), failure = it.failure) }
      .onErrorResume { paymentErrorHandler.handlePaymentConfirmationError(it, command) }
  }
}