package com.ys.paymentservice.infra

import com.ys.paymentservice.application.PaymentConfirmCommand
import com.ys.paymentservice.domain.PSPConfirmationStatus
import com.ys.paymentservice.domain.payment.PaymentExecutionResult
import com.ys.paymentservice.domain.payment.PaymentExtraDetails
import com.ys.paymentservice.domain.payment.PaymentMethod
import com.ys.paymentservice.domain.payment.PaymentType
import com.ys.paymentservice.infra.exception.PSPConfirmationException
import com.ys.paymentservice.infra.exception.TossPaymentError
import com.ys.paymentservice.interfaces.response.TossFailureResponse
import com.ys.paymentservice.interfaces.response.TossPaymentConfirmationResponse
import io.netty.handler.timeout.TimeoutException
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class TossPaymentExecutor(
    private val tossPaymentWebClient: WebClient,
    private val uri: String = "/v1/payments/confirm",
) {

    fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult> {
        return tossPaymentWebClient.post()
            .uri(uri)
            .header("Idempotency-Key", command.orderId)
            .bodyValue("""
        {
          "paymentKey": "${command.paymentKey}",
          "orderId": "${command.orderId}", 
          "amount": ${command.amount}
        }
      """.trimIndent())
            .retrieve()
            .onStatus({ statusCode: HttpStatusCode -> statusCode.is4xxClientError || statusCode.is5xxServerError}) { response ->
                response.bodyToMono(TossFailureResponse::class.java)
                    .flatMap {
                        val error = TossPaymentError.get(it.code)
                        Mono.error<PSPConfirmationException>(
                            PSPConfirmationException(
                                errorCode = error.name,
                                errorMessage = error.description,
                                isSuccess = error.isSuccess(),
                                isFailure = error.isFailure(),
                                isUnknown = error.isUnknown(),
                                isRetryableError = error.isRetryableError()
                            )
                        )
                    }
            }
            .bodyToMono(TossPaymentConfirmationResponse::class.java)
            .map {
                PaymentExecutionResult(
                    paymentKey = command.paymentKey,
                    orderId = command.orderId,
                    extraDetails = PaymentExtraDetails(
                        type = PaymentType.get(it.type),
                        method = PaymentMethod.get(it.method),
                        approvedAt = LocalDateTime.parse(it.approvedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        pspRawData = it.toString(),
                        orderName = it.orderName,
                        pspConfirmationStatus = PSPConfirmationStatus.get(it.status),
                        totalAmount = it.totalAmount.toLong()
                    ),
                    isSuccess = true,
                    isFailure = false,
                    isUnknown = false,
                    isRetryable = false
                )
            }
            .retryWhen(
                Retry.backoff(2, Duration.ofSeconds(1)).jitter(0.1)
                .filter { (it is PSPConfirmationException && it.isRetryableError) || it is TimeoutException }
                .onRetryExhaustedThrow { _, retrySignal ->
                    retrySignal.failure()
                }
            )
    }
}