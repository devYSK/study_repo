package com.fastcampus.helloecommerceservice.exception;

public class NotFoundOrderSheetException extends RuntimeException{
    public NotFoundOrderSheetException(String message) {
        super(message);
    }
}
