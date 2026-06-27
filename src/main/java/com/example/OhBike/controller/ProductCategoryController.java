package com.example.OhBike.controller;


import com.example.OhBike.dto.request.ProductCategoryRequest;
import com.example.OhBike.dto.request.UpdateProductCategoryRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.service.ProductCategoryService;
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
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> createCategory(
            @Valid @RequestBody
            ProductCategoryRequest category,
            Authentication authentication)
    {
        String userEmail = authentication.getName();

        return buildResponse(
                "Category created succesfully",
                HttpStatus.CREATED,
                categoryService.createCategory(category, userEmail)
        );
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllCategories(){
        return buildResponse(
                "Categories found",
                HttpStatus.OK,
                categoryService.getAllCategories()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getCategoryById(@PathVariable UUID id){
        return buildResponse(
                "Category found",
                HttpStatus.OK,
                categoryService.getCategoryById(id)
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> updateCategory(@Valid @RequestBody UpdateProductCategoryRequest category, @PathVariable UUID id, Authentication authentication){
        String userEmail = authentication.getName();
        return buildResponse(
                "Category updated succesfully",
                HttpStatus.OK,
                categoryService.updateCategory(category, id, userEmail)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> deleteCategory(@PathVariable UUID id, Authentication authentication){
        String userEmail = authentication.getName();
        return buildResponse(
                "Category deleted succesfully",
                HttpStatus.OK,
                categoryService.deleteCategory(id, userEmail)
        );
    }

    private ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data){
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
