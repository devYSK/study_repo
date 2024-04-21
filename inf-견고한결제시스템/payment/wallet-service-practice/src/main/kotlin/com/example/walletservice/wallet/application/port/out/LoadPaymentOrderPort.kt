package com.example.walletservice.wallet.application.port.out

import com.example.walletservice.wallet.domain.PaymentOrder

interface LoadPaymentOrderPort {

  fun getPaymentOrders(orderId: String): List<PaymentOrder>
}