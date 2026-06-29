package com.example.OhBike.controller;

import com.example.OhBike.dto.request.ProductVariantRequest;
import com.example.OhBike.dto.request.UpdateProductVariantRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService variantService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SELLER')")
    public ResponseEntity<GeneralResponse> create(@Valid @RequestBody ProductVariantRequest request) {
        return buildResponse("Variant created successfully", HttpStatus.CREATED, variantService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable UUID id) {
        return buildResponse("Variant found", HttpStatus.OK, variantService.getById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<GeneralResponse> getByProduct(@PathVariable UUID productId) {
        return buildResponse("Variants found", HttpStatus.OK, variantService.getByProduct(productId));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<GeneralResponse> getLowStock() {
        return buildResponse("Low stock variants", HttpStatus.OK, variantService.getLowStock());
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SELLER')")
    @PatchMapping("/{id}")
    public ResponseEntity<GeneralResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductVariantRequest request) {
        return buildResponse("Variant updated successfully", HttpStatus.OK, variantService.update(id, request));
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> delete(@PathVariable UUID id) {
        return buildResponse("Variant deactivated successfully", HttpStatus.OK, variantService.delete(id));
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