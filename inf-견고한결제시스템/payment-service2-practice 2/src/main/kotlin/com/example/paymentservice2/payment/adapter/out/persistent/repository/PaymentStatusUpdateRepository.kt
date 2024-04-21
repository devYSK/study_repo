package com.example.paymentservice2.payment.adapter.out.persistent.repository

import com.example.paymentservice2.payment.application.port.out.PaymentStatusUpdateCommand
import reactor.core.publisher.Mono

interface PaymentStatusUpdateRepository {

  fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean>

  fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean>
}