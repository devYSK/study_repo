package com.ys.paymentservice.infra.repository

import com.example.paymentservice2.payment.application.port.out.PaymentStatusUpdateCommand
import com.example.paymentservice2.payment.domain.PaymentEventMessage
import com.example.paymentservice2.payment.domain.PaymentEventMessageType
import com.ys.paymentservice.application.PaymentStatusUpdateCommand
import com.ys.paymentservice.domain.payment.PaymentEventMessage
import com.ys.paymentservice.domain.payment.PaymentEventMessageType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PaymentOutboxRepository {

  fun insertOutbox(command: PaymentStatusUpdateCommand): Mono<PaymentEventMessage>

  fun markMessageAsSent(idempotencyKey: String, type: PaymentEventMessageType): Mono<Boolean>

  fun markMessageAsFailure(idempotencyKey: String, type: PaymentEventMessageType): Mono<Boolean>

  fun getPendingPaymentOutboxes(): Flux<PaymentEventMessage>
}