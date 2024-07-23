package com.example.paymentservice2.payment.adapter.out.stream

import com.example.paymentservice2.payment.domain.PaymentEventMessage
import com.example.paymentservice2.payment.domain.PaymentEventMessageType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
@Tag("ExternalIntegration")
class PaymentEventMessageSenderTest (
  @Autowired private val paymentEventMessageSender: PaymentEventMessageSender
) {


  @Test
  fun `should send eventMessage by using partitionKey`() {
    val paymentEventMessages = listOf(
      PaymentEventMessage(
        type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
        payload = mapOf(
          "orderId" to UUID.randomUUID().toString()
        ),
        metadata = mapOf(
          "partitionKey" to 0
        )
      ),
      PaymentEventMessage(
        type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
        payload = mapOf(
          "orderId" to UUID.randomUUID().toString()
        ),
        metadata = mapOf(
          "partitionKey" to 1
        )
      ),
      PaymentEventMessage(
        type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
        payload = mapOf(
          "orderId" to UUID.randomUUID().toString()
        ),
        metadata = mapOf(
          "partitionKey" to 2
        )
      ),
      PaymentEventMessage(
        type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
        payload = mapOf(
          "orderId" to UUID.randomUUID().toString()
        ),
        metadata = mapOf(
          "partitionKey" to 3
        )
      ),
      PaymentEventMessage(
        type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
        payload = mapOf(
          "orderId" to UUID.randomUUID().toString()
        ),
        metadata = mapOf(
          "partitionKey" to 4
        )
      ), PaymentEventMessage(
        type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
        payload = mapOf(
          "orderId" to UUID.randomUUID().toString()
        ),
        metadata = mapOf(
          "partitionKey" to 5
        )
      )
    )

    paymentEventMessages.forEach {
      paymentEventMessageSender.dispatch(it)
    }

    Thread.sleep(10000)
  }
}