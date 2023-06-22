package com.fastcampus.pass.repository.pass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BulkPassRepository extends JpaRepository<BulkPassEntity, Integer> {

    @Query(value = "select b from BulkPassEntity b " +
            "order by b.startedAt desc")
    List<BulkPassEntity> findAllOrderByStartedAtDesc();
}
