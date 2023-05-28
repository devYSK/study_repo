package com.ys.actuator.httpcounter;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class MyHttpRequestManagerWithoutMicrometer {

	private AtomicLong count = new AtomicLong(0);

	public long getCount() {
		return count.get() + System.currentTimeMillis(); // 값이 변경되는걸 보기 위해 현재시간을 추가함. 원칙적으로는 filter 등을 통해 count값이 변경되어야 함.
	}

	// 아래 메서드는 filter 나 interceptor 등을 통해 http 요청시마다 호출되도록 구현했다고 가정.
	public void increase() {
		count.incrementAndGet();
	}
}