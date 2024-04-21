package com.example.ledgerservice.ledger.application.service

import com.example.ledgerservice.ledger.adapter.out.persistence.repository.SpringDataJpaLedgerEntryRepository
import com.example.ledgerservice.ledger.adapter.out.persistence.repository.SpringDataJpaLedgerTransactionRepository
import com.example.ledgerservice.ledger.application.port.out.DuplicateMessageFilterPort
import com.example.ledgerservice.ledger.application.port.out.LoadAccountPort
import com.example.ledgerservice.ledger.application.port.out.LoadPaymentOrderPort
import com.example.ledgerservice.ledger.application.port.out.SaveDoubleLedgerEntryPort
import com.example.ledgerservice.ledger.domain.*
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
class DoubleLedgerRecordServiceTest (
  @Autowired private val springDataJpaLedgerEntryRepository: SpringDataJpaLedgerEntryRepository,
  @Autowired private val springDataJpaLedgerTransactionRepository: SpringDataJpaLedgerTransactionRepository,
  @Autowired private val duplicateMessageFilterPort: DuplicateMessageFilterPort,
  @Autowired private val loadAccountPort: LoadAccountPort,
  @Autowired private val saveDoubleLedgerEntryPort: SaveDoubleLedgerEntryPort
) {

  @BeforeEach
  fun clean() {
    springDataJpaLedgerEntryRepository.deleteAll()
    springDataJpaLedgerTransactionRepository.deleteAll()
  }

  @Test
  fun `should record double ledger entries successfully`() {
    val paymentEventMessage = PaymentEventMessage(
      type = PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS,
      payload = mapOf(
        "orderId" to UUID.randomUUID().toString()
      )
    )

    val mockLoadPaymentOrderRepository = mockk<LoadPaymentOrderPort>()

    every { mockLoadPaymentOrderRepository.getPaymentOrders(paymentEventMessage.orderId()) } returns listOf(
      PaymentOrder(
        id = 1L,
        amount = 200L,
        orderId = paymentEventMessage.orderId()
      ),
      PaymentOrder(
        id = 2L,
        amount = 300L,
        orderId = paymentEventMessage.orderId()
      )
    )

    val doubleLedgerRecordService = DoubleLedgerEntryRecordService(
      duplicateMessageFilterPort = duplicateMessageFilterPort,
      loadAccountPort = loadAccountPort,
      loadPaymentOrderPort = mockLoadPaymentOrderRepository,
      saveDoubleLedgerEntryPort = saveDoubleLedgerEntryPort
    )

    val ledgerEventMessage = doubleLedgerRecordService.recordDoubleLedgerEntry(paymentEventMessage)

    val jpaLedgerEntryList = springDataJpaLedgerEntryRepository.findAll()

    val sumOf = jpaLedgerEntryList.sumOf {
      when (it.type) {
        LedgerEntryType.CREDIT -> it.amount
        LedgerEntryType.DEBIT -> it.amount.negate()
      }
    }

    assertThat(ledgerEventMessage.type).isEqualTo(LedgerEventMessageType.SUCCESS)
    assertThat(jpaLedgerEntryList.size).isEqualTo(4)
    assertThat(sumOf.toLong()).isEqualTo(0)
  }
}
