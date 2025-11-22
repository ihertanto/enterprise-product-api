package com.ubl.productapi.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.transaction.TransactionSystemException;

import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleNotFound(ResourceNotFoundException ex) {
        Map<String,String> err = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String,String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    // Handle IllegalStateException (used for stock validation and other bad requests)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String,String>> handleBadRequest(IllegalStateException ex) {
        Map<String,String> err = Map.of("error", ex.getMessage() == null ? "Bad request" : ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    // Unwrap common transaction/rollback wrappers to surface validation messages
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Map<String,String>> handleTransactionSystemException(TransactionSystemException ex) {
        Throwable root = ex.getRootCause();
        if (root instanceof IllegalStateException) {
            return handleBadRequest((IllegalStateException) root);
        }
        if (root instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) root;
            // return first violation message
            String msg = cve.getConstraintViolations().stream().findFirst().map(v -> v.getMessage()).orElse("Validation failed");
            return ResponseEntity.badRequest().body(Map.of("error", msg));
        }
        return handleGeneric(ex);
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<Map<String,String>> handleRollbackException(RollbackException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof IllegalStateException) {
            return handleBadRequest((IllegalStateException) cause);
        }
        if (cause instanceof TransactionSystemException) {
            return handleTransactionSystemException((TransactionSystemException) cause);
        }
        return handleGeneric(ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleGeneric(Exception ex) {
        Map<String,String> err = Map.of("error", "Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}