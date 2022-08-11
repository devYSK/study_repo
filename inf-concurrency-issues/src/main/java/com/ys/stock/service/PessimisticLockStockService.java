package com.ys.stock.service;

import com.ys.stock.domain.Stock;
import com.ys.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ysk
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PessimisticLockStockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findByIDWithPessimisticLock(id);
        stock.decrease(quantity);
        log.info("decrese. count :{}", stock.getQuantity());

        stockRepository.saveAndFlush(stock);
    }
}
