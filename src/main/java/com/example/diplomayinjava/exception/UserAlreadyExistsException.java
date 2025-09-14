package com.example.diplomayinjava.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends AuthException {
    
    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT, "USER_ALREADY_EXISTS");
    }
    
    public UserAlreadyExistsException(String field, String value) {
        super(String.format("User with %s '%s' already exists", field, value), 
              HttpStatus.CONFLICT, "USER_ALREADY_EXISTS");
    }
}


