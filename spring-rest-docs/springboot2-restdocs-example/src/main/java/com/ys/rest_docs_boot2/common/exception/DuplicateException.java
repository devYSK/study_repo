package com.ys.rest_docs_boot2.common.exception;

public class DuplicateException extends RuntimeException {

    public DuplicateException(String message, String property) {
        super(message + " by " + property);
    }

}
