package com.example.walletservice.wallet.adapter.`in`.stream

import com.example.walletservice.common.StreamAdapter
import com.example.walletservice.wallet.application.port.`in`.SettlementUseCase
import com.example.walletservice.wallet.domain.PaymentEventMessage
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import java.util.function.Consumer

@Configuration
@StreamAdapter
class PaymentEventMessageHandler (
  private val settlementUseCase: SettlementUseCase,
  private val streamBridge: StreamBridge
) {

  @Bean
  fun consume(): Consumer<Message<PaymentEventMessage>> {
    return Consumer { message ->
      val walletEventMessage = settlementUseCase.processSettlement(message.payload)
      streamBridge.send("wallet", walletEventMessage)
    }
  }
}