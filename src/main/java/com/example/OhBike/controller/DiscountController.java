package com.example.OhBike.controller;

import com.example.OhBike.dto.request.DiscountRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.service.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;

    @PostMapping // Admin only
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> create(@Valid @RequestBody DiscountRequest request) {
        return buildResponse("Discount created successfully", HttpStatus.CREATED, discountService.createDiscount(request ));
    }

    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")// Admin, Seller, Client
    public ResponseEntity<GeneralResponse> findAllActive() {
        return buildResponse("Active discounts retrieved successfully", HttpStatus.OK, discountService.findAllActiveDiscount());
    }

    @GetMapping("/{id}") // Admin, Seller
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GeneralResponse> getByIdDiscount(@PathVariable UUID id) {
        return buildResponse("Discount found successfully", HttpStatus.OK, discountService.getByIdDiscount(id));
    }

    @PutMapping("/{id}") // Admin only
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> update(@PathVariable UUID id, @Valid @RequestBody DiscountRequest request) {
        return buildResponse("Discount updated successfully", HttpStatus.OK, discountService.updateDiscount(id, request));
    }

    @DeleteMapping("/{id}") // Admin only
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> delete(@PathVariable UUID id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Discount deleted successfully")
                .status(HttpStatus.OK.value())
                .time(LocalDateTime.now())
                .build());
    }

    @GetMapping // Admin, Seller, Client
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GeneralResponse> getAllActiveDiscounts() {
        return buildResponse("List of active discounts", HttpStatus.OK, discountService.findAllActiveDiscount());
    }

    private ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data) {
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