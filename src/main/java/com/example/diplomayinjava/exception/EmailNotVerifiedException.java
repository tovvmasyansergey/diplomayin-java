package com.example.diplomayinjava.exception;

import org.springframework.http.HttpStatus;

public class EmailNotVerifiedException extends AuthException {
    
    public EmailNotVerifiedException() {
        super("Email is not verified. Please verify your email first.", 
              HttpStatus.FORBIDDEN, "EMAIL_NOT_VERIFIED");
    }
    
    public EmailNotVerifiedException(String message) {
        super(message, HttpStatus.FORBIDDEN, "EMAIL_NOT_VERIFIED");
    }
}


