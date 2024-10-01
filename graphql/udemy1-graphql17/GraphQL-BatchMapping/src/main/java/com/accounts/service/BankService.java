package com.accounts.service;

import com.accounts.domain.BankAccount;
import com.accounts.domain.Client;
import com.accounts.domain.Currency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BankService {
    private static List<BankAccount> bankAccounts = Arrays.asList(
            new BankAccount("A100", "C100", Currency.USD, 1500.00f, "Active"),
            new BankAccount("A101", "C101", Currency.CAD, 3000.00f, "Active"),
            new BankAccount("A102", "C102", Currency.EUR, 2500.00f, "Inactive"),
            new BankAccount("A103", "C103", Currency.USD, 5000.00f, "Active"),
            new BankAccount("A104", "C104", Currency.EUR, 7500.00f, "Active")
    );

    private static List<Client> clients = Arrays.asList(
            new Client("C100", "A100", "John", "T.", "Doe"),
            new Client("C101", "A101", "Emma", "B.", "Smith"),
            new Client("C102", "A102", "James", "R.", "Brown"),
            new Client("C103", "A103", "Olivia", "S.", "Johnson"),
            new Client("C104", "A104", "William", "K.", "Jones")
    );

    public List<BankAccount> getAccounts() {
        return bankAccounts;
    }

    public Map<BankAccount, Client> getClients (List<BankAccount> accounts) {
        return accounts.stream()
                .collect(Collectors.toMap(
                        account -> account, // Key Mapper
                        account -> clients.stream()
                                .filter(c -> c.getId().equals(account.getClientId()))
                                .findFirst()
                                .orElse(null) // Value Mapper
                ));
    }
}
