package com.example.paymentservice2.payment.adapter.out.persistent

import com.example.paymentservice2.common.PersistentAdapter
import com.example.paymentservice2.payment.adapter.out.persistent.repository.PaymentOutboxRepository
import com.example.paymentservice2.payment.adapter.out.persistent.repository.PaymentRepository
import com.example.paymentservice2.payment.adapter.out.persistent.repository.PaymentStatusUpdateRepository
import com.example.paymentservice2.payment.adapter.out.persistent.repository.PaymentValidationRepository
import com.example.paymentservice2.payment.application.port.out.*
import com.example.paymentservice2.payment.domain.PaymentEvent
import com.example.paymentservice2.payment.domain.PaymentEventMessage
import com.example.paymentservice2.payment.domain.PendingPaymentEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@PersistentAdapter
class PaymentPersistentAdapter (
  private val paymentRepository: PaymentRepository,
  private val paymentStatusUpdateRepository: PaymentStatusUpdateRepository,
  private val paymentValidationRepository: PaymentValidationRepository,
  private val paymentOutboxRepository: PaymentOutboxRepository
) : SavePaymentPort, PaymentStatusUpdatePort, PaymentValidationPort, LoadPendingPaymentPort, LoadPendingPaymentEventMessagePort, LoadPaymentPort, CompletePaymentPort {

  override fun save(paymentEvent: PaymentEvent): Mono<Void> {
    return paymentRepository.save(paymentEvent)
  }

  override fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean> {
    return paymentStatusUpdateRepository.updatePaymentStatusToExecuting(orderId, paymentKey)
  }

  override fun isValid(orderId: String, amount: Long): Mono<Boolean> {
    return paymentValidationRepository.isValid(orderId, amount)
  }

  override fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean> {
    return paymentStatusUpdateRepository.updatePaymentStatus(command)
  }

  override fun getPendingPayments(): Flux<PendingPaymentEvent> {
    return paymentRepository.getPendingPayments()
  }

  override fun getPendingPaymentEventMessage(): Flux<PaymentEventMessage> {
    return paymentOutboxRepository.getPendingPaymentOutboxes()
  }

  override fun getPayment(orderId: String): Mono<PaymentEvent> {
    return paymentRepository.getPayment(orderId)
  }

  override fun complete(paymentEvent: PaymentEvent): Mono<Void> {
    return paymentRepository.complete(paymentEvent)
  }
}