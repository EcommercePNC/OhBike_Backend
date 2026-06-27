package com.example.OhBike.exception;

import com.example.OhBike.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return build(req, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest req) {
        return build(req, HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(
            BusinessRuleException ex, HttpServletRequest req) {
        return build(req, HttpStatus.CONFLICT, ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateField(
            DuplicateFieldException ex, HttpServletRequest req) {
        String code = "DUPLICATE_" + ex.getField().toUpperCase();
        return build(req, HttpStatus.CONFLICT, code, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException ex, HttpServletRequest req) {
        return build(req, HttpStatus.CONFLICT, "DATA_INTEGRITY_VIOLATION",
                "A unique constraint was violated.");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex, HttpServletRequest req) {
        return build(req, HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS",
                "Invalid email or password.");
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(
            AuthorizationDeniedException ex, HttpServletRequest req) {
        return build(req, HttpStatus.FORBIDDEN, "ACCESS_DENIED",
                "You do not have permission to perform this action.");
    }

    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(
            io.jsonwebtoken.ExpiredJwtException ex, HttpServletRequest req) {
        return build(req, HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED",
                "The token has expired. Please log in again.");
    }

    @ExceptionHandler(io.jsonwebtoken.security.SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignature(
            io.jsonwebtoken.security.SignatureException ex, HttpServletRequest req) {
        return build(req, HttpStatus.UNAUTHORIZED, "TOKEN_INVALID_SIGNATURE",
                "Invalid token signature.");
    }

    @ExceptionHandler(io.jsonwebtoken.MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwt(
            io.jsonwebtoken.MalformedJwtException ex, HttpServletRequest req) {
        return build(req, HttpStatus.BAD_REQUEST, "TOKEN_MALFORMED",
                "Token has an invalid format.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        assert ex.getRequiredType() != null;
        String msg = String.format("Parameter '%s' received '%s'; expected type: %s.",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        return build(req, HttpStatus.BAD_REQUEST, "TYPE_MISMATCH", msg);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(
            Exception ex, HttpServletRequest req) {
        return build(req, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "An unexpected error occurred: " + ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> build(
            HttpServletRequest req, HttpStatus status, String code, String message) {

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().format(FMT))
                .status(status.value())
                .code(code)
                .message(message)
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(body);
    }
}