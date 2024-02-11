package com.fastcampus.ecommerce.admin.exception;

public class NotFoundAdminUserException extends RuntimeException{
    public NotFoundAdminUserException(String message) {
        super(message);
    }
}
