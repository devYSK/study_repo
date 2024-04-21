package com.example.paymentservice2.payment.domain

enum class PaymentStatus(description: String) {
  NOT_STARTED("결제 승인 시작 전"),
  EXECUTING("결제 승인 중"),
  SUCCESS("결제 승인 성공"),
  FAILURE("결제 승인 실패"),
  UNKNOWN("결제 승인 알 수 없는 상태");

  companion object {
    fun get(status: String): PaymentStatus {
      return entries.find { it.name == status } ?: throw IllegalArgumentException("PaymentStatus: $status 는 올바르지 않은 결제 타입입니다.")
    }
  }
}
