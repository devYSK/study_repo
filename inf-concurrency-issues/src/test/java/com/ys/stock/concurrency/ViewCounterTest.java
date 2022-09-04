package com.ys.stock.concurrency;

import com.ys.stock.domain.Stock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : ysk
 */
@Slf4j
class ViewCounterTest {


    @DisplayName("view count 테스트")
    @Test
    void countTest() throws InterruptedException {
        int threadCount = 100;

        ViewCounter viewCounter = new ViewCounter();

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                viewCounter.view();
                log.info("currentThreadName : " + Thread.currentThread().getName()
                + ", view Count : " + viewCounter.getViewCount());
            }).start();
        }

        assertEquals(100, viewCounter.getViewCount());
    }


}