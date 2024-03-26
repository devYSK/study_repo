package com.ys.springexample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(threadPoolTaskScheduler());
	}

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10);
		scheduler.setThreadNamePrefix("my-scheduler");
		scheduler.setWaitForTasksToCompleteOnShutdown(true);
		scheduler.setAwaitTerminationSeconds(5); // 5초 대기
		scheduler.setErrorHandler(new SchedulerErrorHandler());
		scheduler.initialize();

		return scheduler;
	}


	public static class SchedulerErrorHandler implements ErrorHandler {

		@Override
		public void handleError(Throwable t) {
			logger.error("Scheduler Error: ", t);
		}
	}

}