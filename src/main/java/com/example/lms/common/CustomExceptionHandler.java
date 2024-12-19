package com.example.lms.common;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handleSecurityException(SecurityException ex) {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
    
    // General Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", "An unexpected error occurred. Please try again later.");
        errorDetails.put("error", ex.getMessage());
        
        // Optionally include stack trace for debugging purposes
        StackTraceElement[] stackTrace = ex.getStackTrace();
        List<String> trace = Arrays.stream(stackTrace)
                .map(StackTraceElement::toString)
                .limit(5) // Limit the stack trace to 5 elements for brevity
                .toList();
        errorDetails.put("stackTrace", trace);
        
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // HTTP Method Not Supported
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>("HTTP method not supported for this endpoint.", HttpStatus.METHOD_NOT_ALLOWED);
    }
    
    // Media Type Not Supported
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return new ResponseEntity<>("Unsupported media type. Please use application/json.", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    
    // Invalid Request Body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidRequestBody(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>("Invalid request body. Please check your input.", HttpStatus.BAD_REQUEST);
    }
    
    // Validation Errors in Query Parameters or Path Variables
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    // Resource Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>("Resource not found.", HttpStatus.NOT_FOUND);
    }
    
    // Access Denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>("Access is denied. You do not have permission to perform this action.", HttpStatus.FORBIDDEN);
    }
    
    // Database Constraint Violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return new ResponseEntity<>("Database error. Please check your input for conflicts.", HttpStatus.CONFLICT);
    }
    
    // JWT or Token-related Errors
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleJwtException(JwtException ex) {
        return new ResponseEntity<>("Invalid or expired token. Please log in again.", HttpStatus.UNAUTHORIZED);
    }
    
    // Missing Request Parameters
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        String errorMessage = String.format("Missing required parameter: %s", ex.getParameterName());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    
}

