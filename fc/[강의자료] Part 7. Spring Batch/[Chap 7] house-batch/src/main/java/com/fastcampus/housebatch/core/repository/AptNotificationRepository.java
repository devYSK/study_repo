package com.fastcampus.housebatch.core.repository;

import com.fastcampus.housebatch.core.entity.AptNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AptNotificationRepository extends JpaRepository<AptNotification, Long> {
    Page<AptNotification> findByEnabledIsTrue(Pageable pageable);
}
