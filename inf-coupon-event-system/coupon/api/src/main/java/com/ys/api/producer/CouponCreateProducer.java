package com.ys.api.producer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponCreateProducer {

	private final KafkaTemplate<String, Long> kafkaTemplate;

	public void create(Long userId) {
		final CompletableFuture<SendResult<String, Long>> future = kafkaTemplate.send("coupon_create", userId);

		try {
			final SendResult<String, Long> stringLongSendResult = future.get();

			System.out.println(stringLongSendResult.toString());
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}

	}

}
