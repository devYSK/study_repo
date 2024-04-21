package com.example.ledgerservice.ledger.domain

data class PaymentEventMessage (
  val type: PaymentEventMessageType,
  val payload: Map<String, Any?> = emptyMap(),
  val metadata: Map<String, Any?> = emptyMap()
) {

  fun orderId(): String = payload["orderId"]!! as String
}

enum class PaymentEventMessageType (description: String) {
  PAYMENT_CONFIRMATION_SUCCESS("결제 승인 완료")
}