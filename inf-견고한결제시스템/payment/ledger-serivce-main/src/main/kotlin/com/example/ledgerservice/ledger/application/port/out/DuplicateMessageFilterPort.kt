package com.example.ledgerservice.ledger.application.port.out

import com.example.ledgerservice.ledger.domain.PaymentEventMessage

interface DuplicateMessageFilterPort {

  fun isAlreadyProcess(paymentEventMessage: PaymentEventMessage): Boolean
}