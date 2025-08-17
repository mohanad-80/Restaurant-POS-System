package com.konecta.internship.Restaurant_POS_System.auth.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}