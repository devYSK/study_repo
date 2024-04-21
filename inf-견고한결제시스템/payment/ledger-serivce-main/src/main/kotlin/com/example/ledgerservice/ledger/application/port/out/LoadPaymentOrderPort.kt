package com.example.ledgerservice.ledger.application.port.out

import com.example.ledgerservice.ledger.domain.PaymentOrder

interface LoadPaymentOrderPort {

  fun getPaymentOrders(orderId: String): List<PaymentOrder>
}