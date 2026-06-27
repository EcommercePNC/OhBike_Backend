package com.example.OhBike.controller;

import com.example.OhBike.dto.request.ProductRequest;
import com.example.OhBike.dto.request.UpdateProductRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.OhBike.service.InventoryService;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final InventoryService inventoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<GeneralResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return buildResponse(
                "Product created successfully",
                HttpStatus.CREATED,
                productService.createProduct(request)
        );
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllProducts(@RequestParam(required = false) UUID categoryId) {
        return buildResponse(
                "Products found",
                HttpStatus.OK,
                productService.getAllProducts(categoryId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getProductById(@PathVariable UUID id) {
        return buildResponse(
                "Product found",
                HttpStatus.OK,
                productService.getProductById(id)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateProduct(
            @Valid @RequestBody UpdateProductRequest request,
            @PathVariable UUID id) {
        return buildResponse(
                "Product updated successfully",
                HttpStatus.OK,
                productService.updateProduct(request, id)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteProduct(@PathVariable UUID id) {
        return buildResponse(
                "Product deleted successfully",
                HttpStatus.OK,
                productService.deleteProduct(id)
        );
    }

    // GET /api/products/{id}/availability
    @GetMapping("/{id}/availability")
    public ResponseEntity<GeneralResponse> getAvailability(@PathVariable UUID id) {
        return buildResponse(
                "Product availability",
                HttpStatus.OK,
                inventoryService.getAvailability(id)
        );
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