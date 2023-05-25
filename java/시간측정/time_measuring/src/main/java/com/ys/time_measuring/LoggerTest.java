package com.ys.time_measuring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

public class LoggerTest {

	private ErrorLogger errorLogger = new ErrorLogger();

	private WarnLogger warnLogger = new WarnLogger();

	private SqlLogger sqlLogger = new SqlLogger();

	private AopLogger aopLogger = new AopLogger();

	public void call(String message) {
		errorLogger.errorLog(message);
		warnLogger.warnLog(message);
		sqlLogger.sqlLog();
		aopLogger.aopWarnLog(message);
	}

	public static void main(String[] args) throws InterruptedException {
		LoggerTest loggerTest = new LoggerTest();


		Thread.sleep(1500);
		loggerTest.call("로그 테스트");
		Thread.sleep(1000);
		loggerTest.call("로그 테스트2");
		Thread.sleep(1000);
		loggerTest.call("로그 테스트3");
		Thread.sleep(1500);

	}

	public static class ErrorLogger {

		private static final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

		public void errorLog(String message) {
			errorLogger.error(message);
		}
	}

	public static class AopLogger {
		private static final Logger logger = LoggerFactory.getLogger("warnLogger");

		public void aopWarnLog(String message) {
			logger.warn(message);
		}
	}

	@Slf4j(topic = "warnLogger")
	public static class WarnLogger {

		public void warnLog(String message) {
			log.warn(message);
		}
	}

	public static class SqlLogger {
		private static final Logger sqlLogger = LoggerFactory.getLogger("sqlLogger");

		public void sqlLog() {
			sqlLogger.debug("execute time 0.01s");
		}
	}
}
