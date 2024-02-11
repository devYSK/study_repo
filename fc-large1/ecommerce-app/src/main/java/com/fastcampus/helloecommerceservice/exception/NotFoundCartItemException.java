package com.fastcampus.helloecommerceservice.exception;

public class NotFoundCartItemException extends RuntimeException{
    public NotFoundCartItemException(String message) {
        super(message);
    }
}
