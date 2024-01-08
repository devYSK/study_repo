package com.ys.consumer.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FaileDEventRepository extends JpaRepository<FailedEvent, Long> {
}
