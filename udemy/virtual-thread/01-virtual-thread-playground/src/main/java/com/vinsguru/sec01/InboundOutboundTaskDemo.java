package com.vinsguru.sec01;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;

/*
    To demo some blocking operations with both platform and virtual threads
 */
public class InboundOutboundTaskDemo {

    private static final int MAX_PLATFORM = 10_000;
    private static final int MAX_VIRTUAL = 100_000;

    public static void main(String[] args) throws InterruptedException {
        // 공용 포크조인 풀 가져오기
        ForkJoinPool commonPool = ForkJoinPool.commonPool();

        // 공용 포크조인 풀의 정보 출력
        System.out.println("공용 포크조인 풀의 병렬성 수준: " + commonPool.getParallelism());
        System.out.println("공용 포크조인 풀의 활성화된 스레드 수: " + commonPool.getActiveThreadCount());
        System.out.println("공용 포크조인 풀의 활성화된 스레드 수: " + commonPool.getRunningThreadCount());
        System.out.println("공용 포크조인 풀의 대기 중인 작업 수: " + commonPool.getPoolSize());

        // platformThreadDemo1();
        // virtualThreadDemo(); // 가상 스레드 데모는 주석 처리되어 있음
    }

    /*
		단순한 자바 플랫폼 스레드를 생성하기
	 */
    private static void platformThreadDemo1(){
        for (int i = 0; i < MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = new Thread(() -> Task.ioIntensive(j));
            thread.start();
        }
    }

    /*
		Thread.Builder를 사용하여 플랫폼 스레드를 생성하기
	*/
    private static void platformThreadDemo2(){
        var builder = Thread.ofPlatform().name("vins", 1);
        for (int i = 0; i < MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = builder.unstarted(() -> Task.ioIntensive(j));
            thread.start();
        }
    }

    /*
		Thread.Builder를 사용하여 플랫폼 데몬 스레드를 생성하기
		- 데몬 스레드는 일반 스레드와 달리 백그라운드에서 실행되며
		  메인 스레드가 종료되면 함께 종료됩니다.
	*/
    private static void platformThreadDemo3() throws InterruptedException {
        var latch = new CountDownLatch(MAX_PLATFORM);
        var builder = Thread.ofPlatform().daemon().name("daemon", 1);
        for (int i = 0; i < MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = builder.unstarted(() -> {
                Task.ioIntensive(j);
                latch.countDown();
            });
            thread.start();
        }
        latch.await(); // 모든 데몬 스레드의 작업이 끝날 때까지 대기
    }

    /*
		Thread.Builder를 사용하여 가상 스레드를 생성하기
		- 가상 스레드는 기본적으로 데몬 스레드입니다.
		- 가상 스레드는 기본적으로 이름이 지정되어 있지 않습니다.
		- 가상 스레드는 경량 스레드로, 많은 수의 동시 작업을 효율적으로 처리할 수 있습니다.
	*/
    private static void virtualThreadDemo() throws InterruptedException {
        var latch = new CountDownLatch(MAX_VIRTUAL);
        var builder = Thread.ofVirtual().name("virtual-", 1);
        for (int i = 0; i < MAX_VIRTUAL; i++) {
            int j = i;
            Thread thread = builder.unstarted(() -> {
                Task.ioIntensive(j);
                latch.countDown();
            });

            thread.start();
        }
        latch.await(); // 모든 가상 스레드의 작업이 끝날 때까지 대기
    }


}
