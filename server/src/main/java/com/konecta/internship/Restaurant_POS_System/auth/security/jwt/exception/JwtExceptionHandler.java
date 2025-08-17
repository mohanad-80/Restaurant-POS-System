package com.konecta.internship.Restaurant_POS_System.auth.security.jwt.exception;


import com.konecta.internship.Restaurant_POS_System.shared.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
@ControllerAdvice
public class JwtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(JwtExceptionHandler.class);

    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleJwtTokenExpired(JwtTokenExpiredException ex) {
        logger.warn("JWT token expired: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("TOKEN_EXPIRED")
                .message("Your session has expired. Please login again.")
                .timestamp(Instant.now())
                .path(getCurrentRequestPath())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(JwtTokenInvalidException.class)
    public ResponseEntity<ErrorResponse> handleJwtTokenInvalid(JwtTokenInvalidException ex) {
        logger.warn("Invalid JWT token: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("INVALID_TOKEN")
                .message("Invalid authentication token. Please login again.")
                .timestamp(Instant.now())
                .path(getCurrentRequestPath())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(JwtTokenMissingException.class)
    public ResponseEntity<ErrorResponse> handleJwtTokenMissing(JwtTokenMissingException ex) {
        logger.warn("Missing JWT token: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("TOKEN_MISSING")
                .message("Authentication token is required to access this resource.")
                .timestamp(Instant.now())
                .path(getCurrentRequestPath())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // Helper method to get current request path
    private String getCurrentRequestPath() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
            return request.getRequestURI();
        } catch (Exception e) {
            return "unknown";
        }
    }
}