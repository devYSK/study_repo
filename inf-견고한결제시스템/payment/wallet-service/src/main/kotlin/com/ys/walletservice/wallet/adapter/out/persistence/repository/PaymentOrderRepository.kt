package com.ys.walletservice.wallet.adapter.out.persistence.repository

import com.ys.walletservice.wallet.domain.PaymentOrder

interface PaymentOrderRepository {

  fun getPaymentOrders(orderId: String): List<PaymentOrder>
}