package com.example.paymentservice2.payment.application.service

import com.example.paymentservice2.common.Logger
import com.example.paymentservice2.common.UseCase
import com.example.paymentservice2.payment.application.port.`in`.PaymentEventMessageRelayUseCase
import com.example.paymentservice2.payment.application.port.out.DispatchEventMessagePort
import com.example.paymentservice2.payment.application.port.out.LoadPendingPaymentEventMessagePort
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.scheduler.Schedulers
import java.util.concurrent.TimeUnit

/**
 * 아웃박스 메시지 릴레이 서비스 Outbox
 */
@UseCase
@Profile("dev")
class PaymentEventMessageRelayService(
    private val loadPendingPaymentEventMessagePort: LoadPendingPaymentEventMessagePort,
    private val dispatchEventMessagePort: DispatchEventMessagePort,
) : PaymentEventMessageRelayUseCase {

    private val scheduler = Schedulers.newSingle("message-relay")

//  @Scheduled(fixedDelay = 180, initialDelay = 180, timeUnit = TimeUnit.SECONDS)
//  override fun relay() {
//    loadPendingPaymentEventMessagePort.getPendingPaymentEventMessage()
//      .map { dispatchEventMessagePort.dispatch(it) }
//      .onErrorContinue { err, _ ->  Logger.error("messageRelay", err.message ?: "failed to relay message.", err)}
//      .subscribeOn(scheduler)
//      .subscribe()
//  }

    @Scheduled(fixedDelay = 10000, initialDelay = 10000, timeUnit = TimeUnit.MILLISECONDS)
    override fun relay() {
        loadPendingPaymentEventMessagePort.getPendingPaymentEventMessage()
            .map {
                Logger.info("messageRelay", "Message to relay: $it")
                dispatchEventMessagePort.dispatch(it)
            }
            .doOnError { err ->
                Logger.error(
                    "messageRelay",
                    err.message ?: "failed to relay message.",
                    err
                )
            } // 오류가 발생한 경우에만 로그를 남깁니다.
            .subscribeOn(scheduler)
            .doOnNext { result ->
                Logger.info(
                    "messageRelay",
                    "Message relayed successfully: $result"
                )
            } // 메시지를 성공적으로 전달한 경우 로그를 남깁니다.
            .subscribe()
    }
}