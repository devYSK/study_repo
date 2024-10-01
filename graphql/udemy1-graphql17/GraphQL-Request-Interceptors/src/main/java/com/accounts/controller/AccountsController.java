package com.accounts.controller;

import com.accounts.domain.Client;
import com.accounts.entity.BankAccount;
import com.accounts.exceptions.AccountNotFoundException;
import com.accounts.exceptions.ClientNotFoundException;
import com.accounts.service.BankService;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
@Slf4j
public class AccountsController {

    @Autowired
    BankService bankService;

    @QueryMapping
    List<BankAccount> accounts (@ContextValue String accountStatus){
        log.info("Getting Accounts for status : " + accountStatus);
        return bankService.getAccounts(accountStatus);
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

    @GraphQlExceptionHandler
    public GraphQLError handle(@NonNull ClientNotFoundException ex, @NonNull DataFetchingEnvironment environment) {
        return GraphQLError
                .newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(ex.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation())
                .build();
    }
}