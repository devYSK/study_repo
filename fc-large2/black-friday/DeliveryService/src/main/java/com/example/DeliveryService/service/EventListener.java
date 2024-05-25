package com.example.DeliveryService.service;

import blackfriday.protobuf.EdaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @KafkaListener(topics = "delivery_request")
    public void consumeDeliveryRequest(byte[] message) throws Exception {
        var object = EdaMessage.DeliveryRequestV1.parseFrom(message);
        logger.info("[delivery_request] consumed: {}", object);

        var delivery = deliveryService.processDelivery(
                object.getOrderId(),
                object.getProductName(),
                object.getProductCount(),
                object.getAddress()
        );

        // 배송 상태 publish
        var deliveryStatusMessage = EdaMessage.DeliveryStatusUpdateV1.newBuilder()
            .setOrderId(delivery.orderId)
            .setDeliveryId(delivery.id)
            .setDeliveryStatus(delivery.status.toString())
            .build();

        kafkaTemplate.send("delivery_status_update", deliveryStatusMessage.toByteArray());
    }
}
