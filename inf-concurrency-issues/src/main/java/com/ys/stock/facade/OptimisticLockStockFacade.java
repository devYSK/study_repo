package com.ys.stock.facade;

import com.ys.stock.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
            }  catch (ObjectOptimisticLockingFailureException e) {
                System.out.println();

                System.out.println();
                System.out.println("예외 발생. ");

                System.out.println();
                System.out.println(e.getClass().getName());
                System.out.println(Objects.requireNonNull(e.getIdentifier()).getClass().getName());
                System.out.println(e.getPersistentClass());
                System.out.println(e.getPersistentClassName());
                System.out.println(e.getIdentifier());
                System.out.println(e.getPersistentClass().getName());

                System.out.println();
                System.out.println();
                Thread.sleep(50);
            }
        }
    }
}
