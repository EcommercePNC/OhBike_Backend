package com.example.OhBike.exception;

import com.example.OhBike.dto.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        //recorre para validar todos los campos
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return buildErrorResponse(request.getRequestURI(), errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateField(DuplicateFieldException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());

        return buildErrorResponse(request.getRequestURI(), errors, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(request.getRequestURI(), ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessRule(BusinessRuleException ex, HttpServletRequest request) {
        return buildErrorResponse(request.getRequestURI(), ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildErrorResponse(request.getRequestURI(), "Data integrity violation. A unique constraint was breached.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildErrorResponse(
                request.getRequestURI(),
                "Invalid username or password.",
                HttpStatus.UNAUTHORIZED
        );
    }

   // JWT Auth filter handler
    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredJwt(io.jsonwebtoken.ExpiredJwtException ex, HttpServletRequest request) {
        return buildErrorResponse(request.getRequestURI(), "The token has expired. Please log in again.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(io.jsonwebtoken.security.SignatureException.class)
    public ResponseEntity<ApiErrorResponse> handleSignatureException(io.jsonwebtoken.security.SignatureException ex, HttpServletRequest request) {
        return buildErrorResponse(request.getRequestURI(), "Invalid token signature.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(io.jsonwebtoken.MalformedJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedJwt(io.jsonwebtoken.MalformedJwtException ex, HttpServletRequest request) {
        return buildErrorResponse(request.getRequestURI(), "Token with invalid format.", HttpStatus.BAD_REQUEST);
    }

    //quitar luego de pruebas ex.message()
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(request.getRequestURI(), "Internal server error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(String uri, Object message, HttpStatus status) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .uri(uri)
                .message(message)
                .status(status.value())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(response);
    }
}