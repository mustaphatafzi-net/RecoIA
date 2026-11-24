
package com.recoia.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UserAlreadyExistsException exception) {
        return new ResponseEntity<>(
                Map.of("error", exception.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException exception) {
        return new ResponseEntity<>(
                Map.of("error", exception.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException exception) {
        return new ResponseEntity<>(
                Map.of("error", exception.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
