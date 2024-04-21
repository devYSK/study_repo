package com.ys.paymentservice.interfaces.request

data class TossPaymentConfirmRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: String
) {

}
