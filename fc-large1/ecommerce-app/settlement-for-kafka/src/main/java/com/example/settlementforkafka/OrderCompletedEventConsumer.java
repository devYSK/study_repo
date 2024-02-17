package com.example.settlementforkafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderCompletedEventConsumer {

    @KafkaListener(
            topics="order-completed-message-v1",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "orderMessageKafkaListenerContainerFactory"
    )
    public void listen(@Payload OrderCompletedMessage orderCompletedMessage) {
        log.info("*************************** 정산 도메인 컨슈머 ****************************");
        log.info("<<< 주문 완료 메세지 수신, {}", orderCompletedMessage);
        log.info("************************************************************************");
    }
}
