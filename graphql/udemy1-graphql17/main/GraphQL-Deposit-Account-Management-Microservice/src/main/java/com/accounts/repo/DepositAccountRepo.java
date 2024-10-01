package com.accounts.repo;


import com.accounts.entity.DepositAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositAccountRepo extends JpaRepository<DepositAccount, String> {
}
