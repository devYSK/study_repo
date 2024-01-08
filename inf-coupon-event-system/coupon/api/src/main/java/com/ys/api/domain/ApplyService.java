package com.ys.api.domain;

import org.springframework.stereotype.Service;

import com.ys.api.producer.CouponCreateProducer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplyService {

	private final CouponRepository couponRepository;

	private final CouponCountRepository couponCountRepository;

	private final CouponCreateProducer couponCreateProducer;

	private final AppliedUserRepository appliedUserRepository;

	public void apply(Long userId) {

		final Long add = appliedUserRepository.add(userId);

		if (add != 1) {
			return;
		}

		Long count = couponCountRepository.increment();

		if (count > 100) {
			return;
		}

		couponCreateProducer.create(userId);
	}

}
