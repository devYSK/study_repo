package com.ys.actuator.guage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

@Configuration
public class GaugeConfigWithMeterBinder {

	@Bean
	public MeterBinder queueSize(QueueManager queueManager) {
		return registry -> Gauge.builder("my.queue2.size",
			queueManager,
			QueueManager::getQueueSize)
			.register(registry);
	}
}