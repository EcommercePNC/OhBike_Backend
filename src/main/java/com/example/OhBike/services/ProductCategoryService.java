package com.example.OhBike.services;

import com.example.OhBike.dto.request.ProductCategoryRequest;
import com.example.OhBike.dto.request.UpdateProductCategoryRequest;
import com.example.OhBike.dto.response.ProductCategoryResponse;

import java.util.List;
import java.util.UUID;

public interface ProductCategoryService {
    ProductCategoryResponse createCategory(ProductCategoryRequest request);
    List<ProductCategoryResponse> getAllCategories();
    ProductCategoryResponse getCategoryById(UUID id);
    ProductCategoryResponse updateCategory(UpdateProductCategoryRequest request, UUID id);
    ProductCategoryResponse deleteCategory(UUID id);
}
