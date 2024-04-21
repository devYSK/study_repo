package com.example.paymentservice2.payment.application.service

import com.example.paymentservice2.common.UseCase
import com.example.paymentservice2.payment.application.port.`in`.PaymentConfirmCommand
import com.example.paymentservice2.payment.application.port.`in`.PaymentRecoveryUseCase
import com.example.paymentservice2.payment.application.port.out.*
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.scheduler.Schedulers
import java.util.concurrent.TimeUnit

@UseCase
@Profile("dev")
class PaymentRecoveryService (
  private val loadPendingPaymentPort: LoadPendingPaymentPort,
  private val paymentValidationPort: PaymentValidationPort,
  private val paymentExecutorPort: PaymentExecutorPort,
  private val paymentStatusUpdatePort: PaymentStatusUpdatePort,
  private val paymentErrorHandler: PaymentErrorHandler
) : PaymentRecoveryUseCase {

  private val scheduler = Schedulers.newSingle("recovery")

  @Scheduled(fixedDelay = 180, initialDelay = 180, timeUnit = TimeUnit.SECONDS)
  override fun recovery() {
    loadPendingPaymentPort.getPendingPayments()
      .map {
        PaymentConfirmCommand(
          paymentKey = it.paymentKey,
          orderId = it.orderId,
          amount = it.totalAmount()
        )
      }
      .parallel(2)
      .runOn(Schedulers.parallel())
      .flatMap { command ->
        paymentValidationPort.isValid(command.orderId, command.amount).thenReturn(command)
          .flatMap { paymentExecutorPort.execute(it) }
          .flatMap { paymentStatusUpdatePort.updatePaymentStatus(PaymentStatusUpdateCommand(it))  }
          .onErrorResume { paymentErrorHandler.handlePaymentConfirmationError(it, command).thenReturn(true) }
      }
      .sequential()
      .subscribeOn(scheduler)
      .subscribe()
  }
}