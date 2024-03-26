package com.ys.springexample;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimpleService {

	private final SimpleService2 simpleService2;

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(SimpleService.class);

	public void call() {
		log.info("Executing service method on thread: {}", Thread.currentThread().getName());
		simpleService2.asyncTask();
		log.info("Service method executed");
	}


	@Service
	@RequiredArgsConstructor
	public static class SimpleService2 {

		private static final Logger log = org.slf4j.LoggerFactory.getLogger(SimpleService2.class);
		private final SimpleService3 simpleService3;
		@Async
		public void asyncTask() {
			log.info("Executing async task on thread: {}", Thread.currentThread().getName());
			simpleService3.asyncTask(5);
		}

	}

	@Service
	public static class SimpleService3 {

		private static final Logger log = org.slf4j.LoggerFactory.getLogger(SimpleService3.class);

		@Async
		public void asyncTask(int param) {
			final int i = ThreadLocalRandom.current()
				.nextInt();
			if (i % 2 == 0) {
				throw new RuntimeException("Random exception");
			}
			log.info("Executing async task on thread: {}", Thread.currentThread().getName());
		}

	}


}
