package com.ys.time_measuring;

// import org.apache.commons.lang3.time.StopWatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.common.base.Stopwatch;
// import org.springframework.util.StopWatch;

// @SpringBootTest
class TimeMeasuringApplicationTests {
	private Service service = new Service();


	@Test
	void currentTimeTest() {
		long startTime = System.currentTimeMillis();

		service.logic0_05Sec();

		long endTime = System.currentTimeMillis();

		long timeElapsed = endTime - startTime;

		System.out.println("milli seconds : " +  timeElapsed);
		System.out.println("seconds : " + (double)timeElapsed / 1000);
	}

	@Test
	void systemNanoTimeTest() {
		long startTime = System.nanoTime();
		service.logic0_05Sec();
		long endTime = System.nanoTime();

		long timeElapsed = endTime - startTime;

		System.out.println("nano seconds :" +  timeElapsed);
		System.out.println("milli seconds: " + timeElapsed / 1000000);
		System.out.println("seconds : " + (double)timeElapsed / 1_000_000_000);
	}

	@Test
	void guavaStopWatchTest() {

		Stopwatch.createUnstarted();

		Stopwatch stopwatch = Stopwatch.createStarted();

		service.logic0_05Sec();
		stopwatch.stop();

		System.out.println(stopwatch.toString());
		System.out.println("nano seconds : " + stopwatch.elapsed(TimeUnit.NANOSECONDS));
		System.out.println("micro seconds : " + stopwatch.elapsed(TimeUnit.MICROSECONDS));
	}

	// @Test
	// void apacheStopWatchTest() {
	//
	// 	StopWatch stopWatch = new StopWatch("thread1 - GET /api/users/1");
	// 	stopWatch.start();
	//
	// 	service.logic0_05Sec();
	//
	// 	stopWatch.stop();
	//
	// 	System.out.println(stopWatch.toString());
	// 	System.out.println("메시지 : " + stopWatch.getMessage() + ", 소요시간 : " + stopWatch.getTime());
	// }

	// @Test
	// void springStopWatchTest() {
	// 	Service service = new Service();
	//
	// 	StopWatch stopWatch = new StopWatch("thread1 - GET /api/users/1");
	// 	stopWatch.start("0.05초 걸리는 유저 조회 시간 측정");
	//
	// 	service.logic0_05Sec();
	//
	// 	stopWatch.stop();
	//
	// 	System.out.println(stopWatch.prettyPrint());
	// 	System.out.println("마지막 작업 걸린 시간 : " + stopWatch.getLastTaskTimeNanos());
	// 	System.out.println("totalTimeSeconds : " + stopWatch.getTotalTimeSeconds());
	//
	// 	System.out.println("------------------");
	//
	// 	stopWatch.start("0.1초 걸리는 유저 조회 시간 측정");
	// 	service.logic0_1Sec();
	// 	stopWatch.stop();
	//
	// 	System.out.println(stopWatch.prettyPrint());
	// 	System.out.println("마지막 작업 걸린 시간 : " + stopWatch.getLastTaskTimeNanos());
	// 	System.out.println("totalTimeSeconds : " + stopWatch.getTotalTimeSeconds());
	// }

	static class Service {

		public void logic0_05Sec() {
			// 0.05초 걸리는 로직을 여기에 작성
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		public void logic0_1Sec() {
			// 0.1초 걸리는 로직을 여기에 작성
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		public void logic0_5Sec() {
			// 0.5초 걸리는 로직을 여기에 작성
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		public void tenSecondLogic() {
			// 10초 걸리는 로직을 여기에 작성
		}

		public void twentySecondLogic() {
			// 20초 걸리는 로직을 여기에 작성
		}

		public void oneMinuteLogic() {
			// 1분 걸리는 로직을 여기에 작성
		}
	}
}

