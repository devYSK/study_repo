package com.ys.stock.service;

import com.ys.stock.domain.Stock;
import com.ys.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : ysk
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW) // syncronized 는 서버가 2대 이상일때 정합성을 보장해주지 못합니다.
    public void decrease(Long id, Long quantity) {

        Stock stock = stockRepository.findById(id).orElseThrow();

        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }

}
