package com.example.ledgerservice.ledger.application.service

import com.example.ledgerservice.common.UseCase
import com.example.ledgerservice.ledger.application.port.`in`.DoubleLedgerEntryRecordUseCase
import com.example.ledgerservice.ledger.application.port.out.DuplicateMessageFilterPort
import com.example.ledgerservice.ledger.application.port.out.LoadAccountPort
import com.example.ledgerservice.ledger.application.port.out.LoadPaymentOrderPort
import com.example.ledgerservice.ledger.application.port.out.SaveDoubleLedgerEntryPort
import com.example.ledgerservice.ledger.domain.*

@UseCase
class DoubleLedgerEntryRecordService (
  private val duplicateMessageFilterPort: DuplicateMessageFilterPort,
  private val loadAccountPort: LoadAccountPort,
  private val loadPaymentOrderPort: LoadPaymentOrderPort,
  private val saveDoubleLedgerEntryPort: SaveDoubleLedgerEntryPort
) : DoubleLedgerEntryRecordUseCase {

  override fun recordDoubleLedgerEntry(message: PaymentEventMessage): LedgerEventMessage {
    if (duplicateMessageFilterPort.isAlreadyProcess(message)) {
      return createLedgerEventMessage(message)
    }

    val doubleAccountsForLedger = loadAccountPort.getDoubleAccountsForLedger(FinanceType.PAYMENT_ORDER)
    val paymentOrders = loadPaymentOrderPort.getPaymentOrders(message.orderId())

    val doubleLedgerEntryList = Ledger.createDoubleLedgerEntry(doubleAccountsForLedger, paymentOrders)

    saveDoubleLedgerEntryPort.save(doubleLedgerEntryList)

    return createLedgerEventMessage(message)
  }

  private fun createLedgerEventMessage(message: PaymentEventMessage): LedgerEventMessage {
    return LedgerEventMessage(
      type = LedgerEventMessageType.SUCCESS,
      payload = mapOf(
        "orderId" to message.orderId()
      )
    )
  }
}