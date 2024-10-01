package com.accounts.repo;

import com.accounts.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByStatus(String status);
}
