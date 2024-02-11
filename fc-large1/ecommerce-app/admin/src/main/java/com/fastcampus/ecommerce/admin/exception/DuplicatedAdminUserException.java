package com.fastcampus.ecommerce.admin.exception;

public class DuplicatedAdminUserException extends RuntimeException{
    public DuplicatedAdminUserException(String message) {
        super(message);
    }
}
