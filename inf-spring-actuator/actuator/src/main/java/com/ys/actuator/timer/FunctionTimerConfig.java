package com.ys.actuator.timer;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.FunctionTimer;
import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class FunctionTimerConfig {

	@Bean
	FunctionTimer myFunctionTimer(MeterRegistry meterRegistry, MyTimerManager myTimerManager) {
		return FunctionTimer.builder("my.timer5.latency",
				myTimerManager,
				MyTimerManager::getCount,
				MyTimerManager::getTotalTime,
				TimeUnit.SECONDS)
			.register(meterRegistry);
	}
}