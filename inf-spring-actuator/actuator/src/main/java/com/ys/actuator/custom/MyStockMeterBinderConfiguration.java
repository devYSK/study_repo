package com.ys.actuator.custom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;

@Configuration
public class MyStockMeterBinderConfiguration {

	@Bean
	public MeterBinder myStockMeterBinder(MyStockManager myStockManager) {
		return registry ->
			Gauge.builder("myStockCount",
				myStockManager,
				MyStockManager::getStock)
				.register(registry);
	}
}
