package com.accounts.controller;

import com.accounts.entity.DepositAccount;
import com.accounts.entity.DepositTransaction;
import com.accounts.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class TransactionController {

    @Autowired
    TransactionService txnService;

    @QueryMapping
    public List<DepositTransaction> getTransactionById (@Argument("accountId")  String accountId){
        log.info("Getting Transactions ");
        return txnService.transactionsByAccountId(accountId);
    }

    @BatchMapping( field = "account", typeName = "DepositTransaction" )
    public Map<DepositTransaction, DepositAccount> getAccountsById(List<DepositTransaction> transactions) {
        log.info("Getting account for DepositTransaction : " + transactions.size());
        return txnService.getTxnAccountMap(transactions);
    }

    @MutationMapping
    public Boolean addTransaction (@Argument("transaction") DepositTransaction transaction) {
        return txnService.save(transaction);
    }
}
