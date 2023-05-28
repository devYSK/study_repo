package com.ys.actuator.queue;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyQueueManagerWithTags {

	private final MeterRegistry meterRegistry;  // 생성자 주입

	public void push() {
		Counter.builder("myQueueCounter")
			.tag("type", "push")   // <-- key value 순
			.register(meterRegistry)
			.increment();
	}

	public void pop() {
		Counter.builder("myQueueCounter")
			.tag("type", "pop") // < --key value 순
			.register(meterRegistry)
			.increment();
	}
}