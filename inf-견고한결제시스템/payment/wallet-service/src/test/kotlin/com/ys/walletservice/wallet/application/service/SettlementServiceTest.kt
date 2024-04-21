package com.ys.walletservice.wallet.application.service

import com.ys.walletservice.wallet.adapter.out.persistence.entity.JpaWalletEntity
import com.ys.walletservice.wallet.adapter.out.persistence.repository.SpringDataJpaWalletRepository
import com.ys.walletservice.wallet.adapter.out.persistence.repository.SpringDataJpaWalletTransactionRepository
import com.ys.walletservice.wallet.application.port.out.DuplicateMessageFilterPort
import com.ys.walletservice.wallet.application.port.out.LoadPaymentOrderPort
import com.ys.walletservice.wallet.application.port.out.LoadWalletPort
import com.ys.walletservice.wallet.application.port.out.SaveWalletPort
import com.ys.walletservice.wallet.domain.PaymentEventMessage
import com.ys.walletservice.wallet.domain.PaymentEventMessageType
import com.ys.walletservice.wallet.domain.PaymentOrder
import com.ys.walletservice.wallet.domain.WalletEventMessageType
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.util.UUID

@SpringBootTest
class SettlementServiceTest (
  @Autowired private val duplicateMessageFilterPort: DuplicateMessageFilterPort,
  @Autowired private val loadWalletPort: LoadWalletPort,
  @Autowired private val saveWalletPort: SaveWalletPort,
  @Autowired private val springDataJpaWalletRepository: SpringDataJpaWalletRepository,
  @Autowired private val springDataJpaWalletTransactionRepository: SpringDataJpaWalletTransactionRepository
) {

  @BeforeEach
  fun clean() {
    springDataJpaWalletTransactionRepository.deleteAll()
    springDataJpaWalletRepository.deleteAll()
  }

  @Test
  fun `should process settlement successfully`() {
    val jpaWalletEntities = listOf(
      JpaWalletEntity(
        userId = 1L,
        balance = BigDecimal.ZERO,
        version = 0,
      ),
      JpaWalletEntity(
        userId = 2L,
        balance = BigDecimal.ZERO,
        version = 0,
      )
    )

    springDataJpaWalletRepository.saveAll(jpaWalletEntities)

    val paymentEventMessage = PaymentEventMessage(
      type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
      payload = mapOf(
        "orderId" to UUID.randomUUID().toString()
      )
    )

    val mockLoadPaymentOrderPort = mockk<LoadPaymentOrderPort>()

    every { mockLoadPaymentOrderPort.getPaymentOrders(paymentEventMessage.orderId()) } returns listOf(
      PaymentOrder(
        id = 1,
        sellerId = 1,
        amount = 3000L,
        orderId = paymentEventMessage.orderId()
      ),
      PaymentOrder(
        id = 2,
        sellerId = 2,
        amount = 4000L,
        orderId = paymentEventMessage.orderId()
      )
    )

    val settlementService = SettlementService(
      duplicateMessageFilterPort = duplicateMessageFilterPort,
      loadWalletPort = loadWalletPort,
      loadPaymentOrderPort = mockLoadPaymentOrderPort,
      saveWalletPort = saveWalletPort
    )

    val walletEventMessage = settlementService.processSettlement(paymentEventMessage)

    val updatedWallets = loadWalletPort.getWallets(jpaWalletEntities.map { it.userId }.toSet())
      .sortedBy { it.userId }

    assertThat(walletEventMessage.payload["orderId"]).isEqualTo(paymentEventMessage.orderId())
    assertThat(walletEventMessage.type).isEqualTo(WalletEventMessageType.SUCCESS)
    assertThat(updatedWallets[0].balance.toInt()).isEqualTo(3000)
    assertThat(updatedWallets[1].balance.toInt()).isEqualTo(4000)
  }

  @Test
  fun `should be processed only once even if it is called multiple times`()  {
    val jpaWalletEntities = listOf(
      JpaWalletEntity(
        userId = 1L,
        balance = BigDecimal.ZERO,
        version = 0,
      ),
      JpaWalletEntity(
        userId = 2L,
        balance = BigDecimal.ZERO,
        version = 0,
      )
    )

    springDataJpaWalletRepository.saveAll(jpaWalletEntities)

    val paymentEventMessage = PaymentEventMessage(
      type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
      payload = mapOf(
        "orderId" to UUID.randomUUID().toString()
      )
    )

    val mockLoadPaymentOrderPort = mockk<LoadPaymentOrderPort>()

    every { mockLoadPaymentOrderPort.getPaymentOrders(paymentEventMessage.orderId()) } returns listOf(
      PaymentOrder(
        id = 1,
        sellerId = 1,
        amount = 3000L,
        orderId = paymentEventMessage.orderId()
      ),
      PaymentOrder(
        id = 2,
        sellerId = 2,
        amount = 4000L,
        orderId = paymentEventMessage.orderId()
      )
    )

    val settlementService = SettlementService(
      duplicateMessageFilterPort = duplicateMessageFilterPort,
      loadWalletPort = loadWalletPort,
      loadPaymentOrderPort = mockLoadPaymentOrderPort,
      saveWalletPort = saveWalletPort
    )

    settlementService.processSettlement(paymentEventMessage)
    settlementService.processSettlement(paymentEventMessage)

    val updatedWallets = loadWalletPort.getWallets(jpaWalletEntities.map { it.userId }.toSet())
      .sortedBy { it.userId }

    assertThat(updatedWallets[0].balance.toInt()).isEqualTo(3000)
    assertThat(updatedWallets[1].balance.toInt()).isEqualTo(4000)
  }
}