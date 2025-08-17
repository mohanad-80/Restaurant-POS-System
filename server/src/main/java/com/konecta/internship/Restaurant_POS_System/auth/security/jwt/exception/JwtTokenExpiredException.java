package com.konecta.internship.Restaurant_POS_System.auth.security.jwt.exception;

public class JwtTokenExpiredException extends RuntimeException {
    public JwtTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}