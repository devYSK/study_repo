package com.example.ledgerservice.ledger.domain

data class DoubleAccountsForLedger (
  val to: Account,
  val from: Account
)