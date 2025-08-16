package com.konecta.internship.Restaurant_POS_System.auth.security.jwt.exception;

public class JwtTokenMissingException extends RuntimeException {
    public JwtTokenMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}