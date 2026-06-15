package com.example.OhBike.controller;

import com.example.OhBike.dto.request.ProductVariantRequestDTO;
import com.example.OhBike.dto.response.ProductVariantResponseDTO;
import com.example.OhBike.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService variantService;

    // POST /api/v1/variants
    @PostMapping
    public ResponseEntity<ProductVariantResponseDTO> create(
            @Valid @RequestBody ProductVariantRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(variantService.create(request));
    }

    // GET /api/v1/variants/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductVariantResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(variantService.getById(id));
    }

    // GET /api/v1/variants/product/{productId}
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductVariantResponseDTO>> getByProduct(
            @PathVariable UUID productId) {
        return ResponseEntity.ok(variantService.getByProduct(productId));
    }

    // GET /api/v1/variants/low-stock
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductVariantResponseDTO>> getLowStock() {
        return ResponseEntity.ok(variantService.getLowStock());
    }

    // PUT /api/v1/variants/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProductVariantResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductVariantRequestDTO request) {
        return ResponseEntity.ok(variantService.update(id, request));
    }

    // DELETE /api/v1/variants/{id}  (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        variantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}