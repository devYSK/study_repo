package com.ys.walletservice.wallet.application.port.out

import com.ys.walletservice.wallet.domain.PaymentOrder

interface LoadPaymentOrderPort {

  fun getPaymentOrders(orderId: String): List<PaymentOrder>
}