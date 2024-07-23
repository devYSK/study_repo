package com.example.paymentservice2.payment.application.service

import com.example.paymentservice2.payment.application.port.`in`.CheckoutCommand
import com.example.paymentservice2.payment.application.port.`in`.CheckoutUseCase
import com.example.paymentservice2.payment.test.PaymentDatabaseHelper
import com.example.paymentservice2.payment.test.PaymentTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import reactor.test.StepVerifier
import java.util.UUID

@SpringBootTest
@Import(PaymentTestConfiguration::class)
class CheckoutServiceTest (
  @Autowired private val checkoutUseCase: CheckoutUseCase,
  @Autowired private val paymentDatabaseHelper: PaymentDatabaseHelper
) {

  @BeforeEach
  fun setup() {
    paymentDatabaseHelper.clean().block()
  }

  @Test
  fun `should save PaymentEvent and PaymentOrder successfully`() {
    val orderId = UUID.randomUUID().toString()
    val checkoutCommand = CheckoutCommand(
      cartId = 1,
      buyerId = 1,
      productIds = listOf(1, 2, 3),
      idempotencyKey = orderId
    )

    StepVerifier.create(checkoutUseCase.checkout(checkoutCommand))
      .expectNextMatches {
        it.amount.toInt() == 60000 && it.orderId == orderId
      }
      .verifyComplete()

    val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

    assertThat(paymentEvent.orderId).isEqualTo(orderId)
    assertThat(paymentEvent.totalAmount()).isEqualTo(60000)
    assertThat(paymentEvent.paymentOrders.size).isEqualTo(checkoutCommand.productIds.size)
    assertFalse(paymentEvent.isPaymentDone())
    assertThat(paymentEvent.paymentOrders.all { !it.isLedgerUpdated() })
    assertThat(paymentEvent.paymentOrders.all { !it.isWalletUpdated() })
  }

  @Test
  fun `should fail to save PaymentEvent and PaymentOrder when trying to save for the second time`() {
    val orderId = UUID.randomUUID().toString()
    val checkoutCommand = CheckoutCommand(
      cartId = 1,
      buyerId = 1,
      productIds = listOf(1, 2, 3),
      idempotencyKey = orderId
    )

    checkoutUseCase.checkout(checkoutCommand).block()

    assertThrows<DataIntegrityViolationException> {
      checkoutUseCase.checkout(checkoutCommand).block()
    }
  }
}