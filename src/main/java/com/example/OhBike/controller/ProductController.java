package com.example.OhBike.controller;

import com.example.OhBike.dto.request.ProductRequest;
import com.example.OhBike.dto.request.UpdateProductRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.service.InventoryService;
import com.example.OhBike.service.ProductService;

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
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final InventoryService inventoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('SELLER')")
    public ResponseEntity<GeneralResponse> createProduct(
            @Valid @RequestBody ProductRequest request,
            Authentication authentication
    ) {

        String sellerEmail = authentication.getName();

        return buildResponse(
                "Product created successfully",
                HttpStatus.CREATED,
                productService.createProduct(request, sellerEmail)
        );
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllPublicProducts(
            @RequestParam(required = false) UUID categoryId
    ) {
        return buildResponse(
                "Products found",
                HttpStatus.OK,
                productService.getAllPublicProducts(categoryId)
        );
    }

    @GetMapping("/my-products")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<GeneralResponse> getSellerProducts(
            Authentication authentication
    ) {

        return buildResponse(
                "Seller products retrieved successfully",
                HttpStatus.OK,
                productService.getProductsBySellerEmail(authentication.getName())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getProductById(
            @PathVariable UUID id
    ) {

        return buildResponse(
                "Product found",
                HttpStatus.OK,
                productService.getProductById(id)
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('SELLER')")
    public ResponseEntity<GeneralResponse> updateProduct(
            @Valid @RequestBody UpdateProductRequest request,
            @PathVariable UUID id,
            Authentication authentication
    ) {

        return buildResponse(
                "Product updated successfully",
                HttpStatus.OK,
                productService.updateProduct(
                        request,
                        id,
                        authentication.getName()
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('SELLER')")
    public ResponseEntity<GeneralResponse> deleteProduct(
            @PathVariable UUID id,
            Authentication authentication
    ) {

        return buildResponse(
                "Product deleted successfully",
                HttpStatus.OK,
                productService.deleteProduct(
                        id,
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<GeneralResponse> getAvailability(
            @PathVariable UUID id
    ) {

        return buildResponse(
                "Product availability",
                HttpStatus.OK,
                inventoryService.getAvailability(id)
        );
    }

    private ResponseEntity<GeneralResponse> buildResponse(
            String message,
            HttpStatus status,
            Object data
    ) {

        String uri =
                ServletUriComponentsBuilder
                        .fromCurrentRequestUri()
                        .build()
                        .getPath();

        return ResponseEntity
                .status(status)
                .body(
                        GeneralResponse.builder()
                                .uri(uri)
                                .message(message)
                                .status(status.value())
                                .time(LocalDateTime.now())
                                .data(data)
                                .build()
                );
    }
}