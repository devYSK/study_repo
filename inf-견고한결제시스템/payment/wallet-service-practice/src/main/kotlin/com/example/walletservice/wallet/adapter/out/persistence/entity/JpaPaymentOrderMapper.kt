package com.example.walletservice.wallet.adapter.out.persistence.entity

import com.example.walletservice.wallet.domain.PaymentOrder
import org.springframework.stereotype.Component

@Component
class JpaPaymentOrderMapper {

  fun mapToDomainEntity(jpaPaymentOrderEntity: JpaPaymentOrderEntity): PaymentOrder {
    return PaymentOrder(
      id = jpaPaymentOrderEntity.id!!,
      sellerId = jpaPaymentOrderEntity.sellerId,
      amount = jpaPaymentOrderEntity.amount.toLong(),
      orderId = jpaPaymentOrderEntity.orderId
    )
  }
}