package com.example.OhBike.controllers;

import com.example.OhBike.dto.request.ProductRequest;
import com.example.OhBike.dto.request.UpdateProductRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteProduct(@PathVariable UUID id) {
        return buildResponse(
                "Product deleted successfully",
                HttpStatus.OK,
                productService.deleteProduct(id)
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