package com.example.paymentservice2.payment.adapter.out.stream

import com.example.paymentservice2.common.Logger
import com.example.paymentservice2.common.StreamAdapter
import com.example.paymentservice2.payment.adapter.out.persistent.repository.PaymentOutboxRepository
import com.example.paymentservice2.payment.application.port.out.DispatchEventMessagePort
import com.example.paymentservice2.payment.domain.PaymentEventMessage
import com.example.paymentservice2.payment.domain.PaymentEventMessageType
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.IntegrationMessageHeaderAccessor
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.FluxMessageChannel
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.scheduler.Schedulers
import reactor.kafka.sender.SenderResult
import java.util.function.Supplier

@Configuration
@StreamAdapter
class PaymentEventMessageSender (
  private val paymentOutboxRepository: PaymentOutboxRepository
) : DispatchEventMessagePort {

  private val sender = Sinks.many().unicast().onBackpressureBuffer<Message<PaymentEventMessage>>()
  private val sendResult = Sinks.many().unicast().onBackpressureBuffer<SenderResult<String>>()

  @Bean
  fun send(): Supplier<Flux<Message<PaymentEventMessage>>> {
    return Supplier {
      sender.asFlux()
        .onErrorContinue { err, _ ->
          Logger.error("sendEventMessage", err.message ?: "failed to send eventMessage", err)
        }
    }
  }

  @Bean(name = ["payment-result"])
  fun sendResultChannel(): FluxMessageChannel {
    return FluxMessageChannel()
  }

  @ServiceActivator(inputChannel = "payment-result")
  fun receiveSendResult(results: SenderResult<String>) {
    if (results.exception() != null) {
      Logger.error("sendEventMessage", results.exception().message ?: "receive an exception for event message send.", results.exception())
    }

    sendResult.emitNext(results, Sinks.EmitFailureHandler.FAIL_FAST)
  }

  @PostConstruct
  fun handleSendResult() {
    sendResult.asFlux()
      .flatMap {
        when (it.recordMetadata() != null) {
          true -> paymentOutboxRepository.markMessageAsSent(it.correlationMetadata(), PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS)
          false -> paymentOutboxRepository.markMessageAsFailure(it.correlationMetadata(), PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS)
        }
      }
      .onErrorContinue { err, _ -> Logger.error("sendEventMessage", err.message ?: "failed to mark the outbox message.", err)  }
      .subscribeOn(Schedulers.newSingle("handle-send-result-event-message"))
      .subscribe()
  }


  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  fun dispatchAfterCommit(paymentEventMessage: PaymentEventMessage) {
    dispatch(paymentEventMessage)
  }

  override fun dispatch(paymentEventMessage: PaymentEventMessage) {
    sender.emitNext(createEventMessage(paymentEventMessage), Sinks.EmitFailureHandler.FAIL_FAST)
  }

  private fun createEventMessage(paymentEventMessage: PaymentEventMessage): Message<PaymentEventMessage> {
    return MessageBuilder.withPayload(paymentEventMessage)
      .setHeader(IntegrationMessageHeaderAccessor.CORRELATION_ID, paymentEventMessage.payload["orderId"])
      .setHeader(KafkaHeaders.PARTITION, paymentEventMessage.metadata["partitionKey"] ?: 0)
      .build()
  }
}