package com.ys.stock.facade;

import com.ys.stock.repository.RedisLockRepository;
import com.ys.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author : ysk
 */
@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;

    private final StockService stockService;

    public void decrease(Long key, Long quantity) throws InterruptedException {
        System.out.println("LettuceLockStockFacade.decrease");
        while (!redisLockRepository.lock(key)) {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(100);
            System.out.println("lockㅌ");
        }

        try {
            System.out.println("call");
            stockService.decrease(key, quantity);

        } finally {
            System.out.println("unlock");
            redisLockRepository.unLock(key);
        }

    }
}
