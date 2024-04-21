package com.ys.paymentservice.domain.payment

import com.ys.paymentservice.application.PaymentStatusUpdateCommand
import com.ys.paymentservice.common.PersistentAdapter
import com.ys.paymentservice.domain.pendingpayment.PendingPaymentEvent
import com.ys.paymentservice.infra.repository.PaymentOutboxRepository
import com.ys.paymentservice.infra.repository.PaymentRepository
import com.ys.paymentservice.infra.repository.PaymentStatusUpdateRepository
import com.ys.paymentservice.infra.repository.PaymentValidationRepository
import com.ys.paymentservice.payment.application.PaymentStatusUpdateCommand
import com.ys.paymentservice.payment.domain.pendingpayment.PendingPaymentEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@PersistentAdapter
class PaymentPersistentAdapter(
    private val paymentRepository: PaymentRepository,
    private val paymentStatusUpdateRepository: PaymentStatusUpdateRepository,
    private val paymentValidationRepository: PaymentValidationRepository,
    private val paymentOutboxRepository: PaymentOutboxRepository,
) {

    fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return paymentRepository.save(paymentEvent)
    }

    fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean> {
        return paymentStatusUpdateRepository.updatePaymentStatusToExecuting(orderId, paymentKey)
    }

    fun isValid(orderId: String, amount: Long): Mono<Boolean> {
        return paymentValidationRepository.isValid(orderId, amount)
    }

    fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean> {
        return paymentStatusUpdateRepository.updatePaymentStatus(command)
    }

    fun getPendingPayments(): Flux<PendingPaymentEvent> {
        return paymentRepository.getPendingPayments()
    }

    fun getPendingPaymentEventMessage(): Flux<PaymentEventMessage> {
        return paymentOutboxRepository.getPendingPaymentOutboxes()
    }

    fun getPayment(orderId: String): Mono<PaymentEvent> {
        return paymentRepository.getPayment(orderId)
    }

    fun complete(paymentEvent: PaymentEvent): Mono<Void> {
        return paymentRepository.complete(paymentEvent)
    }
}