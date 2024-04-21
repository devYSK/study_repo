package com.ys.paymentservice.application

import com.ys.paymentservice.common.UseCase
import com.ys.paymentservice.domain.payment.PaymentPersistentAdapter
import com.ys.paymentservice.infra.TossPaymentExecutor
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.scheduler.Schedulers
import java.util.concurrent.TimeUnit

@UseCase
@Profile("dev")
class PaymentRecoveryService(
    private val loadPendingPaymentPort: PaymentPersistentAdapter,
    private val paymentValidationPort: PaymentPersistentAdapter,
    private val paymentExecutorPort: TossPaymentExecutor,
    private val paymentStatusUpdatePort: PaymentPersistentAdapter,
    private val paymentErrorHandler: PaymentErrorHandler,
) {

    private val scheduler = Schedulers.newSingle("recovery")

    @Scheduled(fixedDelay = 180, initialDelay = 180, timeUnit = TimeUnit.SECONDS)
    fun recovery() {
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
                    .flatMap { paymentStatusUpdatePort.updatePaymentStatus(PaymentStatusUpdateCommand(it)) }
                    .onErrorResume { paymentErrorHandler.handlePaymentConfirmationError(it, command).thenReturn(true) }
            }
            .sequential()
            .subscribeOn(scheduler)
            .subscribe()
    }

}