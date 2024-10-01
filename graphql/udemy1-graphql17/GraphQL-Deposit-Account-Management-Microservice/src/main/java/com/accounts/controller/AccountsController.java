package com.accounts.controller;

import com.accounts.entity.DepositAccount;
import com.accounts.service.DepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Slf4j
public class AccountsController {

    @Autowired
    DepositService depositService;

    @QueryMapping
    public List<DepositAccount> getAllAccounts (){
        log.info("Getting Accounts ");
        return depositService.getAccounts();
    }

    @QueryMapping
    public DepositAccount getAccountById (@Argument("accountID")  String accountID){
        log.info("Getting Account ");
        return depositService.accountById(accountID);
    }

    @MutationMapping
    public Boolean addAccount (@Argument("account") DepositAccount account)  {
        log.info("Saving Account : " + account);
        return depositService.save(account);
    }

    @MutationMapping
    public DepositAccount editAccount (@Argument("account") DepositAccount account) {
        log.info("Editing Account : " + account);
        return depositService.modify(account);
    }

    @MutationMapping
    public Boolean deleteAccount (@Argument("accountID") String accountID) {
        log.info("Deleting Account : " + accountID);
        return depositService.delete(accountID);
    }
}