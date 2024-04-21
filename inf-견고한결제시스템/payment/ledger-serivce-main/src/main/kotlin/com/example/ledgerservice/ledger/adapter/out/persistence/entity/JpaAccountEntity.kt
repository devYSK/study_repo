package com.example.ledgerservice.ledger.adapter.out.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "accounts")
class JpaAccountEntity (
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  val name: String
)