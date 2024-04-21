package com.example.ledgerservice.ledger.adapter.out.persistence.repository

import com.example.ledgerservice.ledger.adapter.out.persistence.entity.JpaPaymentOrderEntity
import com.example.ledgerservice.ledger.adapter.out.persistence.entity.JpaPaymentOrderMapper
import com.example.ledgerservice.ledger.domain.PaymentOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class JpaPaymentOrderRepository (
  private val springDataJpaPaymentOrderRepository: SpringDataJpaPaymentOrderRepository,
  private val jpaPaymentOrderMapper: JpaPaymentOrderMapper
) : PaymentOrderRepository {

  override fun getPaymentOrders(orderId: String): List<PaymentOrder> {
    return springDataJpaPaymentOrderRepository.findByOrderId(orderId)
      .map { jpaPaymentOrderMapper.mapToDomainEntity(it) }
  }
}

interface SpringDataJpaPaymentOrderRepository : JpaRepository<JpaPaymentOrderEntity, Long> {
  fun findByOrderId(orderId: String): List<JpaPaymentOrderEntity>
}