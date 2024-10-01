package com.accounts.domain;



public record BankAccount(String id, String clientId, Currency currency, float balance, String status) {}
