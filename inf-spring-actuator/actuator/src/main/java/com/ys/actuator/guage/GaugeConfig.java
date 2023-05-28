package com.ys.actuator.guage;

import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class GaugeConfig {

	private final QueueManager queueManager;

	private final MeterRegistry meterRegistry;

	@PostConstruct
	public void register() {
		Gauge gauge = Gauge
			.builder("my.queue.size", queueManager,
				queueManager -> queueManager.getQueueSize())
			.register(meterRegistry);

	}
}