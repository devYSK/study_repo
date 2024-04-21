package com.ys.paymentservice.interfaces.request

import java.time.LocalDateTime

/**
 * 사용자가 요청하는 것이 아닌 자동으로 호출되는것을 위해 기본값 설정
 */
data class CheckoutRequest(
    val cartId: Long = 1,
    val productIds: List<Long> = listOf(1,2,3),
    val buyerId: Long = 1,
    val seed: String = LocalDateTime.now().toString(),
) {
}