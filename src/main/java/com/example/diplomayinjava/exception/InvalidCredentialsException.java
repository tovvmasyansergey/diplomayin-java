package com.example.diplomayinjava.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends AuthException {
    
    public InvalidCredentialsException() {
        super("Invalid username or password", HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
    }
    
    public InvalidCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
    }
}


