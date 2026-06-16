package com.example.OhBike.services;

import com.example.OhBike.dto.request.ProductRequest;
import com.example.OhBike.dto.request.UpdateProductRequest;
import com.example.OhBike.dto.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    List<ProductResponse> getAllProducts(UUID categoryId);
    ProductResponse getProductById(UUID id);
    ProductResponse updateProduct(UpdateProductRequest request, UUID id);
    ProductResponse deleteProduct(UUID id);
}
