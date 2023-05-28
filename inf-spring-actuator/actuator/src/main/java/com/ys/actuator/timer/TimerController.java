package com.ys.actuator.timer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class TimerController {

	@Qualifier("myTimer")  // 없어도 오류는 없으나 Timer 타입 bean이 추가되면 오류나므로 예방차원에서 추가.
	private final Timer myTimer;

	private final MeterRegistry meterRegistry;

	@Timed("my.timer3")
	@GetMapping("/timer3/{sleepSeconds}")
	public String timer3(@PathVariable("sleepSeconds") int sleepSeconds) throws InterruptedException {
		Thread.sleep(sleepSeconds);
		return "ok";
	}

	@GetMapping("/timer")
	public String timer() {
		myTimer.record(() -> {
			try {
				// 3초 정도 걸리는 코드가 있다는 가정
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
		return "ok";
	}

	@GetMapping("/timer2")
	public String timer2() {

		Timer.Sample sample = Timer.start(meterRegistry);

		internal();

		sample.stop(meterRegistry.timer("my.timer2"));
		return "ok";
	}

	private void internal() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}