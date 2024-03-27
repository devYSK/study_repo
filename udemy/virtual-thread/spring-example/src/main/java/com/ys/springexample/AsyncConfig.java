package com.ys.springexample;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	@Override
	@Bean(name = "virtualThreadExecutor")
	public Executor getAsyncExecutor() {
		ThreadFactory factory = Thread.ofVirtual().name("virtual-thread", 1)
			.uncaughtExceptionHandler(
				(t, e) -> System.err.println("Uncaught exception in thread " + t.getName() + ": " + e.getMessage()))
			.factory(); // 1은 시작 넘버

		return Executors.newThreadPerTaskExecutor(factory);
	}

	@Bean
	public AsyncTaskExecutor applicationTaskExecutor() {
		return new TaskExecutorAdapter(getAsyncExecutor());
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new CustomAsyncExceptionHandler();
	}

	public static class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

		@Override
		public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
			// 로그 남기기
			System.err.println("Exception Name - " + throwable.getClass().getName());
			System.err.println("Exception message - " + throwable.getMessage());
			System.err.println("Method name - " + method.getName());

			for (Object param : params) {
				System.err.println("Parameter value - " + param);
			}

			Parameter[] parameters = method.getParameters(); // 메서드 파라미터 정보를 가져옴

			for (int i = 0; i < params.length; i++) {
				String paramName = parameters[i].getName(); // 파라미터 이름
				Object paramValue = params[i]; // 파라미터 값
				System.err.println("Parameter name - " + paramName + ", value - " + paramValue);
			}

			try {
				throw (Exception) throwable;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			// 여기에서 추가적인 예외 처리 로직을 구현할 수 있습니다.
			// 예를 들어, 애플리케이션 이벤트를 발행하거나, 알림을 전송할 수 있습니다.
			// eventPublisher.publishEvent(new AsyncErrorEvent(throwable));
		}
	}
}