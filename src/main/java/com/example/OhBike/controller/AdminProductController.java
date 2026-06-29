package com.example.OhBike.controller;

import com.example.OhBike.dto.request.ProductRequest;
import com.example.OhBike.dto.request.UpdateProductRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllProductsAsAdmin(@RequestParam(required = false) UUID categoryId) {
        return buildResponse(
                "All system products retrieved with seller details",
                HttpStatus.OK,
                productService.getAllProductsAsAdmin(categoryId)
        );
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<GeneralResponse> getProductsBySellerAsAdmin(@PathVariable UUID sellerId) {
        return buildResponse(
                "Products for seller " + sellerId + " retrieved",
                HttpStatus.OK,
                productService.getProductsBySellerId(sellerId)
        );
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> createProductAsAdmin(
            @Valid @RequestBody ProductRequest request) {

        return buildResponse(
                "Product created successfully",
                HttpStatus.CREATED,
                productService.createProductAsAdmin(request)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateProductAsAdmin(
            @Valid @RequestBody UpdateProductRequest request,
            @PathVariable UUID id) {

        return buildResponse(
                "Product updated successfully by Admin",
                HttpStatus.OK,
                productService.updateProductAsAdmin(request, id)
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
