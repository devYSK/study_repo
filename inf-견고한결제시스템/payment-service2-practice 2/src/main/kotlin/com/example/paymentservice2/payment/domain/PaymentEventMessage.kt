package com.example.paymentservice2.payment.domain

data class PaymentEventMessage (
  val type: PaymentEventMessageType,
  val payload: Map<String, Any?> = emptyMap(),
  val metadata: Map<String, Any?> = emptyMap()
)

enum class PaymentEventMessageType (description: String) {
  PAYMENT_CONFIRMATION_SUCCESS("결제 승인 완료 이벤트")
}