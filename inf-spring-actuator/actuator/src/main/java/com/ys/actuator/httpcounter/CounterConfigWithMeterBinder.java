package com.ys.actuator.httpcounter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

@Configuration
public class CounterConfigWithMeterBinder {

	@Bean
	public MeterBinder myTimerWithMeterBinder(MyHttpRequestManagerWithoutMicrometer manager) {
		return registry -> FunctionCounter.builder("myHttpRequestWithMeterBinder",
				manager,
				MyHttpRequestManagerWithoutMicrometer::getCount)
			.register(registry);
	}
}