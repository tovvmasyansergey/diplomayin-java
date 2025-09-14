package com.example.diplomayinjava.exception;

import org.springframework.http.HttpStatus;

public class InvalidVerificationCodeException extends AuthException {
    
    public InvalidVerificationCodeException() {
        super("Invalid or expired verification code", 
              HttpStatus.BAD_REQUEST, "INVALID_VERIFICATION_CODE");
    }
    
    public InvalidVerificationCodeException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "INVALID_VERIFICATION_CODE");
    }
}


