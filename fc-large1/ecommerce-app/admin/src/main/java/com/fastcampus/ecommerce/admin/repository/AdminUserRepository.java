package com.fastcampus.ecommerce.admin.repository;

import com.fastcampus.ecommerce.admin.domain.user.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByEmailAndIsActivated(String userEmail, Boolean isActivated);
}
