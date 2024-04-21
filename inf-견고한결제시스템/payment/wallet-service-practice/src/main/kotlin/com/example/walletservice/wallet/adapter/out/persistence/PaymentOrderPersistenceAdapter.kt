package com.example.walletservice.wallet.adapter.out.persistence

import com.example.walletservice.common.PersistenceAdapter
import com.example.walletservice.wallet.adapter.out.persistence.repository.PaymentOrderRepository
import com.example.walletservice.wallet.application.port.out.LoadPaymentOrderPort
import com.example.walletservice.wallet.domain.PaymentOrder

@PersistenceAdapter
class PaymentOrderPersistenceAdapter (
  private val paymentOrderRepository: PaymentOrderRepository
) : LoadPaymentOrderPort {

  override fun getPaymentOrders(orderId: String): List<PaymentOrder> {
    return paymentOrderRepository.getPaymentOrders(orderId)
  }
}