package com.ys.paymentservice.infra.stream

import com.ys.paymentservice.application.PaymentCompleteService
import com.ys.paymentservice.domain.wallet.WalletEventMessage
import org.springframework.context.annotation.Bean
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverOffset
import java.util.function.Function

@Component
class WalletEventMessageHandler (
  private val paymentCompleteUseCase: PaymentCompleteService
) {

  @Bean
  fun wallet(): Function<Flux<Message<WalletEventMessage>>, Mono<Void>> {
    return Function { flux ->
      flux.flatMap { message ->
        paymentCompleteUseCase.completePayment(message.payload)
          .then(Mono.defer { message.headers.get(KafkaHeaders.ACKNOWLEDGMENT, ReceiverOffset::class.java)!!.commit() })
      }.then()
    }
  }
}