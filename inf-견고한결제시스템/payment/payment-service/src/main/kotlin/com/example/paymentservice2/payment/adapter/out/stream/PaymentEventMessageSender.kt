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

  @PostConstruct // 카프카로 보낸 메시지 전송결과 데이터를 받아서 성공하면 성공으로 마킹, 실패하면 실패했다고 마킹.
  fun handleSendResult() {
    sendResult.asFlux()
      .flatMap {
        when (it.recordMetadata() != null) {
          true -> paymentOutboxRepository.markMessageAsSent(it.correlationMetadata(), PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS)
          // message 헤더에 넣엇던 값을 가져올 수 있음.
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

  //  분산 시스템에서 여러 서비스 간의 상호 작용을 추적하기 위해 사용되는 고유 식별자입니다. 이것은 하나의 요청이나 트랜잭션에 대한 모든 관련된 이벤트 또는 로그 메시지에 적용됩니다.
  private fun createEventMessage(paymentEventMessage: PaymentEventMessage): Message<PaymentEventMessage> {
    return MessageBuilder.withPayload(paymentEventMessage)
      .setHeader(IntegrationMessageHeaderAccessor.CORRELATION_ID, paymentEventMessage.payload["orderId"]) // CORRELATION_ID : 메시지가 전송되었는지 추적하기 위한 id
      .setHeader(KafkaHeaders.PARTITION, paymentEventMessage.metadata["partitionKey"] ?: 0)
      .build()
  }
}