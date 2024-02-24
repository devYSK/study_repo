package com.example.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;

@Configuration
public class RedisStreamConfig {

    @Autowired
    private OrderEventStreamListener orderEventStreamListener;

    @Autowired
    private PaymentEventStreamListener paymentEventStreamListener;

    @Bean
    public Subscription orderSubscription(RedisConnectionFactory factory) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();

        StreamMessageListenerContainer listenerContainer = StreamMessageListenerContainer.create(factory, options);

        Subscription subscription = listenerContainer.receiveAutoAck(Consumer.from("notification-service-group", "instance-1"),
                StreamOffset.create("order-events", ReadOffset.lastConsumed()), orderEventStreamListener);

        listenerContainer.start();
        return subscription;
    }

    @Bean
    public Subscription paymentSubscription(RedisConnectionFactory factory) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();

        StreamMessageListenerContainer listenerContainer = StreamMessageListenerContainer.create(factory, options);

        Subscription subscription = listenerContainer.receiveAutoAck(Consumer.from("notification-service-group", "instance-1"),
                StreamOffset.create("payment-events", ReadOffset.lastConsumed()), paymentEventStreamListener);

        listenerContainer.start();
        return subscription;
    }
}
