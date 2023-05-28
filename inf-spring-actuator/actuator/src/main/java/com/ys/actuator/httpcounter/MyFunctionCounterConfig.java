package com.ys.actuator.httpcounter;

import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MyFunctionCounterConfig {

	private final MyHttpRequestManagerWithoutMicrometer myManager;

	private final MeterRegistry meterRegistry;

	@PostConstruct
	void init() {
		FunctionCounter.builder("myHttpRequestWithoutMicrometer",
				myManager, MyHttpRequestManagerWithoutMicrometer::getCount)
			.register(meterRegistry);
	}
}