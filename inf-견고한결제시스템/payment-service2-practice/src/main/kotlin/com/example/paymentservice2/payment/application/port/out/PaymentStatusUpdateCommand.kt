package com.example.paymentservice2.payment.application.port.out

import com.example.paymentservice2.payment.domain.PaymentExecutionResult
import com.example.paymentservice2.payment.domain.PaymentExtraDetails
import com.example.paymentservice2.payment.domain.PaymentFailure
import com.example.paymentservice2.payment.domain.PaymentStatus

data class PaymentStatusUpdateCommand (
  val paymentKey: String,
  val orderId: String,
  val status: PaymentStatus,
  val extraDetails: PaymentExtraDetails? = null,
  val failure: PaymentFailure? = null
) {

  constructor(paymentExecutionResult: PaymentExecutionResult) : this(
    paymentKey = paymentExecutionResult.paymentKey,
    orderId = paymentExecutionResult.orderId,
    status = paymentExecutionResult.paymentStatus(),
    extraDetails = paymentExecutionResult.extraDetails,
    failure = paymentExecutionResult.failure
  )

  init {
    require(status == PaymentStatus.SUCCESS || status == PaymentStatus.FAILURE || status == PaymentStatus.UNKNOWN) {
      "결제 상태 (status: $status) 는 올바르지 않은 결제 상태입니다."
    }

    if (status == PaymentStatus.SUCCESS) {
      requireNotNull(extraDetails) { "PaymentStatus 값이 SUCCESS 라면 PaymentExtraDetails 는 null 이 되면 안됩니다." }
    } else if (status == PaymentStatus.FAILURE) {
      requireNotNull(failure) { "PaymentStatus 값이 FAILURE 라면 PaymentExecutionFailure 는 null 이 되면 안됩니다. "}
    }
  }
}