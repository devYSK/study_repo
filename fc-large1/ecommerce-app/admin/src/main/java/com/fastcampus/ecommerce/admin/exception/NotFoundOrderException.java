package com.fastcampus.ecommerce.admin.exception;

public class NotFoundOrderException extends RuntimeException{
    public NotFoundOrderException(String message) {
        super(message);
    }
}
