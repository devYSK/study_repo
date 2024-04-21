package com.example.paymentservice2.payment.adapter.out.persistent.repository

import com.example.paymentservice2.payment.application.port.out.PaymentStatusUpdateCommand
import com.example.paymentservice2.payment.domain.PaymentEventMessage
import com.example.paymentservice2.payment.domain.PaymentEventMessageType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PaymentOutboxRepository {

  fun insertOutbox(command: PaymentStatusUpdateCommand): Mono<PaymentEventMessage>

  fun markMessageAsSent(idempotencyKey: String, type: PaymentEventMessageType): Mono<Boolean>

  fun markMessageAsFailure(idempotencyKey: String, type: PaymentEventMessageType): Mono<Boolean>

  fun getPendingPaymentOutboxes(): Flux<PaymentEventMessage>
}