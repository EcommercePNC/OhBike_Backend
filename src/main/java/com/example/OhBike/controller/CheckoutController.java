package com.example.OhBike.controller;

import com.example.OhBike.dto.request.CheckoutRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.service.CheckoutValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutValidationService checkoutValidationService;

    @PostMapping("/validate")
    public ResponseEntity<GeneralResponse> validate(
            @Valid @RequestBody CheckoutRequest request,
            Authentication authentication) {
        String email = authentication.getName();

        return buildResponse(
                "Checkout validation result",
                HttpStatus.OK,
                checkoutValidationService.validate(request, email)
        );
    }

    private ResponseEntity<GeneralResponse> buildResponse(
            String message, HttpStatus status, Object data) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity.status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(LocalDateTime.now())
                        .data(data)
                        .build());
    }
}