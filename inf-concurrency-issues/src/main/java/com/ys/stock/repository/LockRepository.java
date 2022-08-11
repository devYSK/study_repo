package com.ys.stock.repository;

import com.ys.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author : ysk
 */
public interface LockRepository extends JpaRepository<Stock, Long> {

    @Query(value = "select get_lock(:key, 1000)", nativeQuery = true)
    void getLock(@Param("key") String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(@Param("key") String key);

}