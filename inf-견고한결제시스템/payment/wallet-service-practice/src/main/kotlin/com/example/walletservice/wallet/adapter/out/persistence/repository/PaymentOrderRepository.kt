package com.example.walletservice.wallet.adapter.out.persistence.repository

import com.example.walletservice.wallet.domain.PaymentOrder

interface PaymentOrderRepository {

  fun getPaymentOrders(orderId: String): List<PaymentOrder>
}