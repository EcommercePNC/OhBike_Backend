package com.example.OhBike.service;

import com.example.OhBike.dto.request.ProductRequest;
import com.example.OhBike.dto.request.UpdateProductRequest;
import com.example.OhBike.dto.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request, String sellerEmail );
    List<ProductResponse> getAllProducts(UUID categoryId);
    ProductResponse getProductById(UUID id);
    ProductResponse updateProduct(UpdateProductRequest request, UUID id, String sellerEmail);
    ProductResponse deleteProduct(UUID id, String sellerEmail);
}
