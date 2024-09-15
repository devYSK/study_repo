package com.yscof.ses.common.validator;

import java.util.regex.Pattern;

public class EmailValidator {
    
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(String email) {
        if (email == null){ 
            return false;
        }

        return EMAIL_PATTERN.matcher(email).matches();
    }

}
