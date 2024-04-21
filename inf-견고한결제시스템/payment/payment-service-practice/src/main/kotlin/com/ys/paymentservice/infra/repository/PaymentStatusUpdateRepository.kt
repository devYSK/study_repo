package com.ys.paymentservice.infra.repository

import com.example.paymentservice2.payment.application.port.out.PaymentStatusUpdateCommand
import com.ys.paymentservice.application.PaymentStatusUpdateCommand
import reactor.core.publisher.Mono

interface PaymentStatusUpdateRepository {

  fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean>

  fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean>
}