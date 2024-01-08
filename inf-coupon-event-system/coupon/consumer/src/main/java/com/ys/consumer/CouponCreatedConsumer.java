package com.ys.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ys.consumer.domain.Coupon;
import com.ys.consumer.domain.CouponRepository;
import com.ys.consumer.domain.FaileDEventRepository;
import com.ys.consumer.domain.FailedEvent;

@Component
public class CouponCreatedConsumer {

	private final CouponRepository couponRepository;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final FaileDEventRepository faileDEventRepository;

	public CouponCreatedConsumer(final CouponRepository couponRepository,
		final FaileDEventRepository faileDEventRepository) {
		this.couponRepository = couponRepository;
		this.faileDEventRepository = faileDEventRepository;
	}

	@KafkaListener(topics = "coupon_create", groupId = "group_1")
	public void listener(Long userId) {

		try {
			couponRepository.save(new Coupon(userId));
		} catch (Exception e) {
			logger.error("failed to create coupon::" + userId);

			faileDEventRepository.save(new FailedEvent(userId));
		}
	}

}
