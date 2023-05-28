package com.ys.actuator.timer;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.FunctionTimer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

@Configuration
public class TimerConfigWithMeterBinder {

	@Bean
	public MeterBinder myTimerWithMeterBinder(MyTimerManager myTimerManager) {
		return registry -> {
			FunctionTimer functionTimer = FunctionTimer.builder("my.timerWithMeterBinder.latency",
					myTimerManager,
					MyTimerManager::getCount,
					MyTimerManager::getTotalTime,
					TimeUnit.SECONDS)
				.register(registry);
		};
	}
}
