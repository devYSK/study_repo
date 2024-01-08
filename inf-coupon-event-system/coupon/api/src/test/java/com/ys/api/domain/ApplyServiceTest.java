package com.ys.api.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ApplyServiceTest {

	@Autowired
	private ApplyService applyService;

	@Autowired
	private CouponRepository couponRepository;

	@Transactional
	@Test
	void 한번만응모() {
		applyService.apply(1L);

		final long count = couponRepository.count();

		assertThat(count).isEqualTo(1);
	}

	@Transactional
	@Test
	void 여러명응모() throws InterruptedException {
		int threadCount = 1000;
		final ExecutorService executorService = Executors.newFixedThreadPool(32);
		final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			long userId = i;

			executorService.submit(() -> {
				try {
					applyService.apply(1L);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();

		Thread.sleep(1000);
		final long count = couponRepository.count();

		assertThat(count).isEqualTo(1);
	}

}