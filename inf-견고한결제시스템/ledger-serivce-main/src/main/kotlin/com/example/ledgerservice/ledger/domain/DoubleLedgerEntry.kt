package com.example.ledgerservice.ledger.domain

data class DoubleLedgerEntry (
  val credit: LedgerEntry,
  val debit: LedgerEntry,
  val transaction: LedgerTransaction
) {

  init {
    require(credit.amount == debit.amount)  {
      "a double leger entry require that the amounts for both the credit and the debit are same"
    }
  }
}
