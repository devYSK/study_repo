package com.fastcampus.ecommerce.admin.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@ControllerAdvice
public class AdminControllerAdvice {
    @ExceptionHandler({Exception.class})
    protected ResponseStatusException handleException(Exception e) {
        log.info(">>> Server Exception Handle: {}", e.getMessage());
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
