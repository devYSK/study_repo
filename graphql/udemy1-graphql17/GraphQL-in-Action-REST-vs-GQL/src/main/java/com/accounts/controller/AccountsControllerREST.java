package com.accounts.controller;

import java.util.List;

import com.accounts.domain.BankAccount;
import com.accounts.domain.Client;
import com.accounts.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AccountsControllerREST {
    @Autowired
    private BankService bankService;


    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ResponseEntity<List<BankAccount>> getAllAccounts() {
        log.info("Getting all accounts");
        List<BankAccount> accounts = bankService.getAccounts();
        return ResponseEntity.ok(accounts); // Returns the list of accounts with an HTTP 200 status
    }

    @RequestMapping(value = "/accounts/{accountId}/client", method = RequestMethod.GET)
    public ResponseEntity<Client> getClientByAccountId(@PathVariable String accountId) {
        log.info("Getting client for account with ID: {}", accountId);
        Client client = bankService.getClientByAccountId(accountId);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build(); // Returns an HTTP 404 status if the client is not found
        }
    }
}
