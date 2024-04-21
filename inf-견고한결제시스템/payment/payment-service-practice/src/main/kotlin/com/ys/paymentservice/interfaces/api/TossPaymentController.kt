package com.ys.paymentservice.interfaces.api

import com.ys.paymentservice.common.WebAdapter
import com.ys.paymentservice.interfaces.request.TossPaymentConfirmRequest
import com.ys.paymentservice.infra.TossPaymentExecutor
import com.ys.paymentservice.interfaces.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

// 결제 승인 요청을 받는 컨트롤러
@WebAdapter
@RestController
@RequestMapping("/v1/toss")
class TossPaymentController(
    private val tossPaymentExecutor: TossPaymentExecutor,
) {

    @PostMapping("/confirm") // success.html에서 결제 승인 요청을 보내는 API
    fun confirm(@RequestBody request: TossPaymentConfirmRequest): Mono<ResponseEntity<ApiResponse<String>>> {

        return tossPaymentExecutor.execute(
            paymentKey = request.paymentKey,
            orderId = request.orderId,
            amount = request.amount.toLong()
        ).map { paymentKey ->
            ResponseEntity.ok().body(
                ApiResponse(
                    status = HttpStatus.OK.value(),
                    data = paymentKey
                )
            )
        }
    }
}