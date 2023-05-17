package com.ys.time_measuring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OtherLogTest {


	public void call() {
		log.info("info call");
		log.debug("debug call");
		log.warn("warn call");
		System.out.println(log);
		System.out.println(log.getClass());
		System.out.println(log.getClass().getName());
		System.out.println(log.getClass().getSimpleName());

	}

	static class Log4jLogger {
		private static Logger logger = LogManager.getLogger(Log4jLogger.class);

		void method() {
			logger.info("debug log : {}", 1);
		}
	}

	public static void main(String[] args) {
		Log4jLogger log4jLogger = new Log4jLogger();

		log4jLogger.method();
	}
}
