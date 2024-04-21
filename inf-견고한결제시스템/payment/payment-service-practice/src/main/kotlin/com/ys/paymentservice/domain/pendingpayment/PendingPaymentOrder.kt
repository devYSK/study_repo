package com.ys.paymentservice.domain.pendingpayment

data class PendingPaymentOrder (
  val paymentOrderId: Long,
  val status: PaymentStatus,
  val amount: Long,
  val failedCount: Byte,
  val threshold: Byte
)
