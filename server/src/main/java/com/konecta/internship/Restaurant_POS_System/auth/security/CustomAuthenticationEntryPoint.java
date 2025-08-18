package com.konecta.internship.Restaurant_POS_System.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konecta.internship.Restaurant_POS_System.auth.security.jwt.exception.JwtTokenMissingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public CustomAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Check if a JWT exception was attached by the filter
        Exception jwtException = (Exception) request.getAttribute("exception");
        if (jwtException != null) {
            resolver.resolveException(request, response, null, jwtException);
        } else {
            // If no token was provided at all, we want TOKEN_MISSING instead of 500
            if (authException instanceof InsufficientAuthenticationException) {
                resolver.resolveException(request, response, null,
                        new JwtTokenMissingException("Authentication token is required"));
            } else {
                resolver.resolveException(request, response, null, authException);
            }
        }
    }
}