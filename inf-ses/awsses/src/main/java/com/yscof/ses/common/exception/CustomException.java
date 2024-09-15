package com.yscof.ses.common.exception;

import com.yscof.ses.common.code.CInterface;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final CInterface codeInterface;

    public CustomException(CInterface codeInterface) {
        super(codeInterface.getMessage());
        this.codeInterface = codeInterface;
    }

    public CustomException(CInterface codeInterface, String additionalMessage) {
        super(codeInterface.getMessage() + additionalMessage);
        this.codeInterface = codeInterface;
    }

}
