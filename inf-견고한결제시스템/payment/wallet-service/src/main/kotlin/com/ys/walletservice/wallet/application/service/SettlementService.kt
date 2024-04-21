package com.ys.walletservice.wallet.application.service

import com.ys.walletservice.common.UseCase
import com.ys.walletservice.wallet.application.port.`in`.SettlementUseCase
import com.ys.walletservice.wallet.application.port.out.DuplicateMessageFilterPort
import com.ys.walletservice.wallet.application.port.out.LoadPaymentOrderPort
import com.ys.walletservice.wallet.application.port.out.LoadWalletPort
import com.ys.walletservice.wallet.application.port.out.SaveWalletPort
import com.ys.walletservice.wallet.domain.*

@UseCase
class SettlementService (
  private val duplicateMessageFilterPort: DuplicateMessageFilterPort,
  private val loadPaymentOrderPort: LoadPaymentOrderPort,
  private val loadWalletPort: LoadWalletPort,
  private val saveWalletPort: SaveWalletPort
) : SettlementUseCase {

  override fun processSettlement(paymentEventMessage: PaymentEventMessage): WalletEventMessage {
    if (duplicateMessageFilterPort.isAlreadyProcess(paymentEventMessage)) {
      return createWalletEventMessage(paymentEventMessage)
    }

    val paymentOrders = loadPaymentOrderPort.getPaymentOrders(paymentEventMessage.orderId())
    val paymentOrdersBySellerId = paymentOrders.groupBy { it.sellerId }

    val updatedWallets = getUpdatedWallets(paymentOrdersBySellerId)

    saveWalletPort.save(updatedWallets)

    return createWalletEventMessage(paymentEventMessage)
  }

  private fun createWalletEventMessage(paymentEventMessage: PaymentEventMessage) =
    WalletEventMessage(
      type = WalletEventMessageType.SUCCESS,
      payload = mapOf(
        "orderId" to paymentEventMessage.orderId()
      )
    )

  private fun getUpdatedWallets(paymentOrdersBySellerId: Map<Long, List<PaymentOrder>>): List<Wallet> {
    val sellerIds = paymentOrdersBySellerId.keys

    val wallets = loadWalletPort.getWallets(sellerIds)

    return wallets.map { wallet ->
      wallet.calculateBalanceWith(paymentOrdersBySellerId[wallet.userId]!!)
    }
  }

}