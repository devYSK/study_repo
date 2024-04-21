package com.example.ledgerservice.ledger.adapter.out.persistence.entity

import com.example.ledgerservice.ledger.domain.PaymentOrder
import org.springframework.stereotype.Component

@Component
class JpaPaymentOrderMapper {

  fun mapToDomainEntity(jpaPaymentOrderEntity: JpaPaymentOrderEntity): PaymentOrder {
    return PaymentOrder(
      id = jpaPaymentOrderEntity.id!!,
      orderId = jpaPaymentOrderEntity.orderId,
      amount = jpaPaymentOrderEntity.amount.toLong()
    )
  }
}