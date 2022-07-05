package com.ys.springbootshop.exception;

/**
 * @author : ysk
 */
public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String message) {
        super(message);
    }

}
