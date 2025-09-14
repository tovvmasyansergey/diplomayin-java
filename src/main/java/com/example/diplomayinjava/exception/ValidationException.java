package com.example.diplomayinjava.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String errorCode = "VALIDATION_ERROR";
    private final List<String> errors;
    
    public ValidationException(String message) {
        super(message);
        this.errors = List.of(message);
    }
    
    public ValidationException(List<String> errors) {
        super("Validation failed: " + String.join(", ", errors));
        this.errors = errors;
    }
}


