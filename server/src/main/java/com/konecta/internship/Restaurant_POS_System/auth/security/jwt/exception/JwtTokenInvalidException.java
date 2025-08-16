package com.konecta.internship.Restaurant_POS_System.auth.security.jwt.exception;

public class JwtTokenInvalidException extends RuntimeException {
    public JwtTokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
