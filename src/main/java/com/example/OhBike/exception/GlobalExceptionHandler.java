package com.example.OhBike.exception;

import com.example.OhBike.dto.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
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

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(UnauthorizedOperationException ex, HttpServletRequest request) {
        return buildErrorResponse(request.getRequestURI(), ex.getMessage(), HttpStatus.UNAUTHORIZED);
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request)
    {
        String message = ex.getMessage() != null && ex.getMessage().contains("Required request body is missing")
                ? "Request body is required."
                : "Request body is malformed.";

        return buildErrorResponse(request.getRequestURI(), message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                request.getRequestURI(),
                "You do not have permission to perform this action.",
                HttpStatus.FORBIDDEN
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
        return buildErrorResponse(request.getRequestURI(), "Internal server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        assert ex.getRequiredType() != null;
        String message = String.format("The parameter '%s' has an invalid value '%s'. Expected type: %s.",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        return buildErrorResponse(request.getRequestURI(), message, HttpStatus.BAD_REQUEST);
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