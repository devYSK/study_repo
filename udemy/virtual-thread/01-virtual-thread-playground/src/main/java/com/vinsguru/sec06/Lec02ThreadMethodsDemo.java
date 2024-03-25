package com.vinsguru.sec06;

import com.vinsguru.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
    Quick demo to show few useful thread methods
*/
/*
    몇 가지 유용한 스레드 메소드를 보여주는 간단한 데모
*/
public class Lec02ThreadMethodsDemo {

    private static final Logger log = LoggerFactory.getLogger(Lec02ThreadMethodsDemo.class);

    public static void main(String[] args) throws InterruptedException {
        join();
        CommonUtils.sleep(Duration.ofSeconds(1));
    }

    /*
        스레드가 가상인지 확인하기
     */
    private static void isVirtual() {
        var t1 = Thread.ofVirtual().start(() -> CommonUtils.sleep(Duration.ofSeconds(2)));
        var t2 = Thread.ofPlatform().start(() -> CommonUtils.sleep(Duration.ofSeconds(2)));
        log.info("t1이 가상 스레드인가: {}", t1.isVirtual());
        log.info("t2가 가상 스레드인가: {}", t2.isVirtual());
        log.info("현재 스레드가 가상 스레드인가: {}", Thread.currentThread().isVirtual());
    }


    /*
        여러 시간이 많이 걸리는 I/O 호출을 가상 스레드로 오프로드하고 완료될 때까지 기다립니다.
        참고: 실제 애플리케이션에서는 더 나은 방법을 사용할 수 있습니다.
        이것은 간단한 thread.join() 데모입니다.
     */
    private static void join() throws InterruptedException {
        var t1 = Thread.ofVirtual().start(() -> {
            log.info("제품 서비스 호출됨 {}", Thread.currentThread());
            CommonUtils.sleep(Duration.ofSeconds(5));
            log.info("제품 서비스 종료 {}", Thread.currentThread());

        });
        var t2 = Thread.ofVirtual().start(() -> {
            log.info("가격 서비스 호출됨 {}", Thread.currentThread());
            CommonUtils.sleep(Duration.ofSeconds(3));
            log.info("가격 서비스 종료 {}", Thread.currentThread());
        });
        t1.join(); // 이 스레드가 종료될 때까지 기다립니다
        System.out.println("t1 조인");
        t2.join(); // 이 스레드가 종료될 때까지 기다립니다
        System.out.println("t2 조인");
    }

    private static void join2() throws InterruptedException, ExecutionException {
        ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

        try {
            CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
                log.info("제품 서비스 호출됨 {}", Thread.currentThread());
                CommonUtils.sleep(Duration.ofSeconds(5));
                log.info("제품 서비스 종료 {}", Thread.currentThread());
            }, virtualExecutor);

            CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
                log.info("가격 서비스 호출됨 {}", Thread.currentThread());
                CommonUtils.sleep(Duration.ofSeconds(3));
                log.info("가격 서비스 종료 {}", Thread.currentThread());
            }, virtualExecutor);

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2);
            allFutures.join(); // 모든 스레드(작업)가 완료될 때까지 기다립니다.

            System.out.println("모든 스레드 작업 완료");
        } finally {
            virtualExecutor.shutdown(); // 작업이 끝났으니 ExecutorService를 종료합니다.
        }
    }

    /*
        스레드 실행을 중단/중지하기
        경우에 따라, 자바는 InterruptedException, IOException, SocketException 등을 던질 수 있습니다.

        현재 스레드가 중단되었는지 확인할 수도 있습니다.
        Thread.currentThread().isInterrupted() - boolean을 반환합니다.

        while(!Thread.currentThread().isInterrupted()){
            작업 계속하기
            ...
            ...
        }
     */
    private static void interrupt() {
        var t1 = Thread.ofVirtual().start(() -> {
            CommonUtils.sleep(Duration.ofSeconds(2));
            log.info("제품 서비스 호출됨");
        });
        log.info("t1이 중단됐는가: {}", t1.isInterrupted());
        t1.interrupt();
        log.info("t1이 중단됐는가: {}", t1.isInterrupted());
    }

}