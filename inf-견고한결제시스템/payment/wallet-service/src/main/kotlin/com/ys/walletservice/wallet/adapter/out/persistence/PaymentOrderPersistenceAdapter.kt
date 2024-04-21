package com.ys.walletservice.wallet.adapter.out.persistence

import com.ys.walletservice.common.PersistenceAdapter
import com.ys.walletservice.wallet.adapter.out.persistence.repository.PaymentOrderRepository
import com.ys.walletservice.wallet.application.port.out.LoadPaymentOrderPort
import com.ys.walletservice.wallet.domain.PaymentOrder

@PersistenceAdapter
class PaymentOrderPersistenceAdapter (
  private val paymentOrderRepository: PaymentOrderRepository
) : LoadPaymentOrderPort {

  override fun getPaymentOrders(orderId: String): List<PaymentOrder> {
    return paymentOrderRepository.getPaymentOrders(orderId)
  }
}