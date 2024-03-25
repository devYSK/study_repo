package com.vinsguru.sec06;

import com.vinsguru.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ThreadFactory;

public class Lec01ThreadFactory {

    private static final Logger log = LoggerFactory.getLogger(Lec01ThreadFactory.class);

    public static void main(String[] args) {
        // 가상 스레드를 활용한 데모 실행
        demo(Thread.ofVirtual().name("vins", 1).factory());

        // 3초 동안 대기
        CommonUtils.sleep(Duration.ofSeconds(3));
    }

    /*
        몇 개의 스레드를 생성합니다.
        각 스레드는 1개의 자식 스레드를 생성합니다.
        이것은 간단한 데모입니다. 실제로는 ExecutorService 등을 사용합시다.
        가상 스레드는 생성하기에 비용이 적게 듭니다.
     */
    private static void demo(ThreadFactory factory){
        for (int i = 0; i < 30; i++) {
            var t = factory.newThread(() -> {
                log.info("작업이 시작됐습니다. {}", Thread.currentThread()); // "Task started."
                var ct = factory.newThread(() -> {
                    log.info("자식 작업이 시작됐습니다. {}", Thread.currentThread()); // "Child task started."
                    CommonUtils.sleep(Duration.ofSeconds(2));
                    log.info("자식 작업이 끝났습니다. {}", Thread.currentThread()); // "Child task ended."
                });
                ct.start();
                log.info("작업이 끝났습니다. {}", Thread.currentThread()); // "Task ended."
            });
            t.start();
        }
    }

}
