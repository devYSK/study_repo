package com.accounts.exceptions;

public class AccountNotFountException extends RuntimeException {
    public AccountNotFountException(String message) {
        super(message);
    }
}