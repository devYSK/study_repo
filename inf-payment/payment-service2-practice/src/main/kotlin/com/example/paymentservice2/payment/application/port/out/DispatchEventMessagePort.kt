package com.example.paymentservice2.payment.application.port.out

import com.example.paymentservice2.payment.domain.PaymentEventMessage

interface DispatchEventMessagePort {

  fun dispatch(paymentEventMessage: PaymentEventMessage)
}