package com.ys.time_measuring;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ysk")
public class LogTest {

private OtherLogTest otherLog = new OtherLogTest();

	public void call() {
		log.info("info call");
		log.debug("debug call");
		log.warn("warn call");
		System.out.println(log);
		System.out.println(log.getClass());
		System.out.println(log.getClass().getName());
		System.out.println(log.getClass().getSimpleName());

		otherLog.call();
	}

}
