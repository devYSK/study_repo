package com.vinsguru.sec09;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vinsguru.util.CommonUtils;


/*
    Scoped Values inheritance using Structured Task Scope
 */
public class Lec07ScopedValuesWithStructuredTaskScope {

	private static final Logger log = LoggerFactory.getLogger(Lec07ScopedValuesWithStructuredTaskScope.class);
	private static final ScopedValue<String> SESSION_TOKEN = ScopedValue.newInstance();

	public static void main(String[] args) {

		ScopedValue.runWhere(SESSION_TOKEN, "token-123", Lec07ScopedValuesWithStructuredTaskScope::task);

	}

	private static void task() {
		try (var taskScope = new StructuredTaskScope<>()) { // 가상스레드를 생성할 수 있는 스코프 생성.

			log.info("token: {}", SESSION_TOKEN.get());

			// 하위작업 생성
			var subtask1 = taskScope.fork(Lec07ScopedValuesWithStructuredTaskScope::getDeltaAirfare);
			var subtask2 = taskScope.fork(Lec07ScopedValuesWithStructuredTaskScope::getFrontierAirfare);

			taskScope.join(); // 하위작업이 끝날떄까지 대기

			log.info("subtask1 state: {}", subtask1.state()); // UNAVAILABLE, SUCCESS, FAIL 을 반환
			log.info("subtask2 state: {}", subtask2.state());

			log.info("subtask1 result: {}", subtask1.get());
			log.info("subtask2 result: {}", subtask2.get());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String getDeltaAirfare() {
		var random = ThreadLocalRandom.current()
			.nextInt(100, 1000);
		log.info("delta: {}", random);
		log.info("detal token: {}", SESSION_TOKEN.get());
		CommonUtils.sleep("delta", Duration.ofSeconds(1));
		return "Delta-$" + random;
	}

	private static String getFrontierAirfare() {
		var random = ThreadLocalRandom.current()
			.nextInt(100, 1000);
		log.info("frontier: {}", random);
		log.info("frontier token: {}", SESSION_TOKEN.get());
		CommonUtils.sleep("frontier", Duration.ofSeconds(2));
		failingTask();
		return "Frontier-$" + random;
	}

	private static String failingTask() {
		throw new RuntimeException("oops");
	}

}
