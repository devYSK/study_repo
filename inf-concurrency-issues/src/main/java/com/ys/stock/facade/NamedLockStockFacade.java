package com.ys.stock.facade;

import com.ys.stock.repository.LockRepository;
import com.ys.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * @author : ysk
 */
@RequiredArgsConstructor
@Component
public class NamedLockStockFacade {

    private final LockRepository lockRepository;

    private final StockService stockService;


    public void decrease(Long id, Long quantity) {
        try {
            lockRepository.getLock(id.toString());
            stockService.decrease(id, quantity);

        } finally {
            lockRepository.releaseLock(id.toString());
        }
    }
}
