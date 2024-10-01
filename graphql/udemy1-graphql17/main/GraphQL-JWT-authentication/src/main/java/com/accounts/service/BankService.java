package com.accounts.service;

import com.accounts.domain.BankAccount;
import com.accounts.domain.Client;
import com.accounts.domain.Currency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BankService {

    // Immutable lists for bank accounts and clients
    private static final List<BankAccount> bankAccounts = List.of(
            new BankAccount("A100", "C100", Currency.USD, 106.00f, "A"),
            new BankAccount("A101", "C200", Currency.CAD, 250.00f, "A"),
            new BankAccount("A102", "C300", Currency.CAD, 333.00f, "I"),
            new BankAccount("A103", "C400", Currency.EUR, 4000.00f, "A"),
            new BankAccount("A104", "C500", Currency.EUR, 4000.00f, "A")
    );
    private static final List<Client> clients = List.of(
            new Client("C100", "A100", "Elena", "Maria", "Gonzalez"),
            new Client("C200", "A101", "James", "Robert", "Smith"),
            new Client("C300", "A102", "Aarav", "Kumar", "Patel"),
            new Client("C400", "A103", "Linh", "Thi", "Nguyen"),
            new Client("C500", "A104", "Olivia", "Grace", "Johnson")
    );

    // Method to get all bank accounts
    public List<BankAccount> getAccounts() {
        return bankAccounts;
    }

    // Method to get client by account ID
    public Client getClientByAccountId(String accountId) {
        return clients.stream()
                .filter(c -> c.accountId().equals(accountId))
                .findFirst()
                .orElse(null);
    }
}

