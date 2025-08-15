package com.konecta.internship.Restaurant_POS_System;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String message;
        if (exception instanceof UsernameNotFoundException) {
            message = "User not found";
        } else if (exception instanceof BadCredentialsException) {
            message = "Invalid username or password";
        } else {
            message = "Authentication failed";
        }

        response.getWriter().write(
                String.format("{\"path\":\"%s\",\"error\":\"Unauthorized\",\"message\":\"%s\",\"status\":401}",
                        request.getRequestURI(), message)
        );
    }
}