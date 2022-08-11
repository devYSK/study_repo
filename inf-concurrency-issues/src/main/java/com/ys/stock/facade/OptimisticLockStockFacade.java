package com.ys.stock.facade;

import com.ys.stock.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author : ysk
 */
@Service
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try
            {
                optimisticLockStockService.decrease(id, quantity);
                break;
            }  catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
