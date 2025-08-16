package com.konecta.internship.Restaurant_POS_System.auth.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.konecta.internship.Restaurant_POS_System.shared.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(403)
                .error("ACCESS_DENIED")
                .message("You don't have permission to access this route")
                .timestamp(Instant.now())
                .path(request.getServletPath())
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
}