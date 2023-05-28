package com.ys.actuator.custom;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MyCustomHealthIndicator implements HealthIndicator {
	@Override
	public Health health() {
		boolean status = getStatus();
		if (status) {
			return Health.up()
				.withDetail("key1", "value1")
				.withDetail("key2", "value2")
				.build();
		}

		return Health.down()
			.withDetail("key3", "value3")
			.withDetail("key4", "value4")
			.build();
	}

	boolean getStatus() {
		// 현재시각이 짝수,홀수인지에 따라 up, down을 판단하는 것으로 대체
		return System.currentTimeMillis() % 2 == 0;
	}
}