package com.example.OhBike.service;

import com.example.OhBike.dto.request.ProductVariantRequest;
import com.example.OhBike.dto.request.UpdateProductVariantRequest;
import com.example.OhBike.dto.response.ProductVariantResponse;

import java.util.List;
import java.util.UUID;

public interface ProductVariantService {

    ProductVariantResponse create(ProductVariantRequest request);

    ProductVariantResponse getById(UUID id);

    List<ProductVariantResponse> getByProduct(UUID productId);

    List<ProductVariantResponse> getLowStock();

    ProductVariantResponse update(UUID id, UpdateProductVariantRequest request);

    ProductVariantResponse delete(UUID id); // soft delete, retorna el estado final
}