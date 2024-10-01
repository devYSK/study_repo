package com.accounts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Data
public class BankAccount {
    private  String id;
    private String clientId;
    private Currency currency;
    private Float balance;
    private String status;
}
