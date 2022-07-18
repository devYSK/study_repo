package com.ys.springboot_blog_hodol.response;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *     "code" : "400",
 *     "message" : "잘못된 요청"
 *     "validation" : {
 *
 *     }
 * }
 * @author : ysk
 */
@Getter
public class ErrorResponse {

    private final String code;

    private final String message;

    private Map<String, String> validation = new HashMap<>();

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
