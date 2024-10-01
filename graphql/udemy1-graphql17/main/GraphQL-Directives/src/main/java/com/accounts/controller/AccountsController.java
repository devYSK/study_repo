package com.accounts.controller;

import com.accounts.domain.Client;
import com.accounts.entity.BankAccount;
import com.accounts.service.BankService;
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
public class AccountsController {

    @Autowired
    BankService bankService;

    @QueryMapping
    List<BankAccount> accounts (){
        log.info("Getting Accounts ");
        return bankService.getAccounts();
    }

    @QueryMapping
    BankAccount accountById (@Argument("accountId")  Long accountId){
        log.info("Getting Account ");
        return bankService.accountById(accountId);
    }

    @BatchMapping( field = "client", typeName = "BankAccountType" )
    public Map<BankAccount, Client> clients(List<BankAccount> bankAccounts) {
        log.info("Getting client for Accounts : " + bankAccounts.size());
        return bankService.getBankAccountClientMap(bankAccounts);
    }

    @MutationMapping
    Boolean addAccount (@Argument("account") BankAccount account)  {
        log.info("Saving Account : " + account);
        bankService.save(account);
        return true;
    }

    @MutationMapping
    BankAccount editAccount (@Argument("account") BankAccount account) {
        log.info("Editing Account : " + account);
        return bankService.modify(account);
    }

    @MutationMapping
    Boolean deleteAccount (@Argument("id") Long accountId) {
        log.info("Deleting Account : " + accountId);
        return bankService.delete(accountId);
    }
}