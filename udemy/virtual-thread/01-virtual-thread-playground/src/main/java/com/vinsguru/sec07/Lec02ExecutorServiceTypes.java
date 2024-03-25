package com.vinsguru.sec07;

import com.vinsguru.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/*
    다양한 ExecutorService 타입과 그 사용 사례에 대해 논의
 */

public class Lec02ExecutorServiceTypes {

    private static final Logger log = LoggerFactory.getLogger(Lec02ExecutorServiceTypes.class);

    public static void main(String[] args) {
        // main 메소드에서 각 메소드를 호출하여 ExecutorService 타입을 시험해 볼 수 있습니다.
    }

    // 단일 스레드 실행자 - 작업을 순차적으로 실행하기 위함
    private static void single(){
        execute(Executors.newSingleThreadExecutor(), 3);
        // 사용 사례: 작업 실행 순서가 중요할 때 사용.
        // 생성 비용: 낮음. 단일 스레드만 유지하므로 오버헤드가 적습니다.
    }

    // 고정 스레드 풀
    private static void fixed(){
        execute(Executors.newFixedThreadPool(5), 20);
        // 사용 사례: 동시에 실행할 작업의 최대 수가 정해져 있을 때 사용.
        // 생성 비용: 중간. 고정된 수의 스레드를 미리 생성하고 관리해야 하므로 일정한 오버헤드가 있습니다.
    }

    // 탄력적 스레드 풀
    private static void cached(){
        execute(Executors.newCachedThreadPool(), 200);
        // 사용 사례: 실행해야 할 작업의 수가 불규칙하거나 예측 불가능할 때 사용.
        // 생성 비용: 높음. 필요에 따라 스레드 수가 자동으로 조절되므로 관리 오버헤드가 증가할 수 있습니다.
    }

    // 작업 당 가상 스레드를 생성하는 ExecutorService
    private static void virtual(){
        execute(Executors.newVirtualThreadPerTaskExecutor(), 10_000);
        // 사용 사례: 매우 많은 수의 짧은 작업을 처리해야 할 때 사용.
        // 생성 비용: 매우 낮음. 가상 스레드는 경량이며, 생성과 소멸 비용이 매우 낮습니다.
    }

    // 주기적인 작업을 스케줄링
    private static void scheduled(){
        try(var executorService = Executors.newSingleThreadScheduledExecutor()){
            executorService.scheduleAtFixedRate(() -> {
                log.info("실행 중인 작업");
            }, 0, 1, TimeUnit.SECONDS);

            CommonUtils.sleep(Duration.ofSeconds(5));
        }
        // 사용 사례: 주기적으로 반복해야 하는 작업을 스케줄링할 때 사용.
        // 생성 비용: 낮음. 주기적인 작업을 관리하는 데 필요한 리소스가 적습니다.
    }

    private static void execute(ExecutorService executorService, int taskCount){
        try(executorService){
            for (int i = 0; i < taskCount; i++) {
                int j = i;
                executorService.submit(() -> ioTask(j));
            }
            log.info("작업 제출 완료");
        }
    }

    private static void ioTask(int i){
        log.info("작업 시작: {}. 스레드 정보 {}", i, Thread.currentThread());
        CommonUtils.sleep(Duration.ofSeconds(5));
        log.info("작업 종료: {}. 스레드 정보 {}", i, Thread.currentThread());
    }

}
