package com.accounts.service;

import com.accounts.entity.DepositAccount;
import com.accounts.entity.DepositTransaction;
import com.accounts.repo.DepositTransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TransactionService {

    @Autowired
    DepositTransactionRepo transactionRepository;

    @Autowired
    DepositService service;

    public Boolean save(DepositTransaction transaction) {
        if (!Objects.isNull(service.accountById(transaction.getAccountId()))  &&
              ! transactionRepository.existsByAccountIdAndTransactionId(transaction.getAccountId(), transaction.getTransactionId())){
            transactionRepository.save(transaction);
            return true;
        }

        return false;
    }

    public List<DepositTransaction>  transactionsByAccountId(String accountId) {
            return transactionRepository.findByAccountId(accountId);
    }

    public Map<DepositTransaction, DepositAccount> getTxnAccountMap(List<DepositTransaction> transactions) {
        Map<DepositTransaction, DepositAccount> depTxnAccountMap = new HashMap<>();

        for (DepositTransaction txn : transactions) {
            DepositAccount account = service.accountById(txn.getAccountId());

            if (!Objects.isNull(account)) {
                depTxnAccountMap.put(txn, account);
            }
        }

        return depTxnAccountMap;
    }
}
