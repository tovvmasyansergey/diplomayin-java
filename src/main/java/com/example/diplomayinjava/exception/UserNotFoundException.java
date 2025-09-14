package com.example.diplomayinjava.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AuthException {
    
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }
    
    public UserNotFoundException(String field, String value) {
        super(String.format("User not found with %s: %s", field, value), 
              HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }
}


