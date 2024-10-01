package com.accounts.service;

import com.accounts.entity.DepositAccount;
import com.accounts.repo.DepositAccountRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DepositService {
    @Autowired
    DepositAccountRepo repo;

    public Boolean save(DepositAccount account) {
        if (!repo.findById(account.getAccountId()).isPresent()){
            repo.save(account);
            return true;
        }
        return false;
    }

    public DepositAccount modify(DepositAccount account) {
        repo.save(account);
        return account;
    }

    public List<DepositAccount> getAccounts() {
        return repo.findAll();
    }

    public DepositAccount accountById(String accountId) {
        return repo.findById(accountId).get();
    }

    public Boolean delete(String accountId) {
        if (repo.findById(accountId).isPresent()){
            repo.delete(repo.findById(accountId).get());
            return true;
        }
        return false;
    }
}