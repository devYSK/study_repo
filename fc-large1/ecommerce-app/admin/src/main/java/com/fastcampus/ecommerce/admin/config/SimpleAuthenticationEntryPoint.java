package com.fastcampus.ecommerce.admin.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public static final String XML_HTTP_REQUEST_VALUE = "XMLHttpRequest";
    public static final String X_REQUESTED_WITH_HEADER_KEY = "x-requested-with";
    public static final String CUSTOMER_LOGIN = "/users/login";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (XML_HTTP_REQUEST_VALUE.equals(request.getHeader(X_REQUESTED_WITH_HEADER_KEY))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        response.sendRedirect(CUSTOMER_LOGIN);
    }
}
