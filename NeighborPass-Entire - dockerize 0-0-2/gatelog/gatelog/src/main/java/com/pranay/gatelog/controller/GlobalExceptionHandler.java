package com.pranay.gatelog.controller;


import com.pranay.gatelog.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)  // Catch your custom or generic runtime exceptions
    public ResponseEntity<Map<String, String>> handleNotFoundException(RuntimeException ex) {
        // Create a custom error object for better API responses
        Map<String,String> map = new HashMap<>();
        map.put("ErrorResponse", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler(RuntimeException.class)  // Catch your custom or generic runtime exceptions
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        // Create a custom error object for better API responses
        Map<String,String> map = new HashMap<>();
        map.put("ErrorResponse", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);

    }

    //@ExceptionHandler(Exception.class)  // Fallback for unexpected errors
    public ResponseEntity<?> handleGenericException(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("An unexpected error occurred");

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

// Simple ErrorResponse class for standardization
class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
    // Getters
}
