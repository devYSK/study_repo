package com.ys.stock.service;

import com.ys.stock.domain.Stock;
import com.ys.stock.facade.OptimisticLockStockFacade;
import com.ys.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : ysk
 */
@SpringBootTest
class StockServiceTest {


    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PessimisticLockStockService pessimisticLockStockService;

    @Autowired
    private OptimisticLockStockFacade optimisticLockStockFacade;

    @BeforeEach
    public void before() {
        Stock stock = new Stock(1L, 100L);
        stockRepository.save(stock);
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    void stock_decrease() {
        stockService.decrease(1L, 1L);

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(99, stock.getQuantity());
    }

    @Test
    void 동시에_100개의_요청_OPTIMISTIC_LOCK() throws InterruptedException {
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {

            executorService.submit(() -> {
                try {
                    optimisticLockStockFacade.decrease(1L, 1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }


        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0L, stock.getQuantity());
    }

    @Test
    void 동시에_100개의_요청_PESSIMISTIC_LOCK() throws InterruptedException {
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {

            executorService.submit(() -> {
                try {
                    pessimisticLockStockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }


        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0L, stock.getQuantity());
    }

    @Test
    void 동싱에_100개의_요청_NON() throws InterruptedException {
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            try {
                executorService.submit(() -> {
                    stockService.decrease(1L, 1L);
                });
            } finally {
                latch.countDown();
            }
        }


        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0L, stock.getQuantity());

    }
}