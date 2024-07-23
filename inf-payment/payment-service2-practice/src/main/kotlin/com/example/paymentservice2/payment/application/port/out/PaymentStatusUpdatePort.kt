package com.example.paymentservice2.payment.application.port.out

import reactor.core.publisher.Mono

interface PaymentStatusUpdatePort {

  fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean>

  fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean>
}