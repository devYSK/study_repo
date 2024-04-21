package com.example.ledgerservice.ledger.adapter.out.persistence

import com.example.ledgerservice.common.PersistenceAdapter
import com.example.ledgerservice.ledger.adapter.out.persistence.repository.PaymentOrderRepository
import com.example.ledgerservice.ledger.application.port.out.LoadPaymentOrderPort
import com.example.ledgerservice.ledger.domain.PaymentOrder

@PersistenceAdapter
class PaymentOrderPersistentAdapter (
  private val paymentOrderRepository: PaymentOrderRepository
) : LoadPaymentOrderPort {

  override fun getPaymentOrders(orderId: String): List<PaymentOrder> {
    return paymentOrderRepository.getPaymentOrders(orderId)
  }
}