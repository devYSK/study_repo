package com.ys.springexample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SimpleScheduler {

	private static final Logger log = LoggerFactory.getLogger(SimpleScheduler.class);

	// 예제 스케줄된 작업
	// @Scheduled(fixedDelay = 1000)
	public void scheduledTask() {
		log.info("Executing scheduled task on thread: {}", Thread.currentThread().getName());
	}
}
