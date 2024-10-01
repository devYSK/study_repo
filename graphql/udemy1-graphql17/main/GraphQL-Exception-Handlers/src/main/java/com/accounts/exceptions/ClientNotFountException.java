package com.accounts.exceptions;

public class ClientNotFountException extends RuntimeException {
    public ClientNotFountException(String message) {
        super(message);
    }
}