package com.fastcampus.helloecommerceservice.exception;


public class NotFoundProductException extends RuntimeException{
    public NotFoundProductException(String message) {
        super(message);
    }
}
