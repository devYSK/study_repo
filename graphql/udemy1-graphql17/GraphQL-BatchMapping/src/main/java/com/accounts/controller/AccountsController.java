package com.accounts.controller;

import com.accounts.domain.BankAccount;
import com.accounts.domain.Client;
import com.accounts.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
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

    /** Get clients with N + 1 problem
    @SchemaMapping (typeName = "BankAccount", field = "client")
    Client getClient (BankAccount account) {
        log.info("Getting client for " + account.getId());
        return bankService.getClientByAccountId(account.getId());
    }*/


    /** Get clients without N + 1 problem **/
    @BatchMapping( field = "client")
    Map<BankAccount, Client> getClient (List<BankAccount> accounts){
        log.info("Getting client for Accounts : " + accounts.size());

        Map<BankAccount, Client> clentsMap = bankService.getClients (accounts);
        return clentsMap;
    }
}
