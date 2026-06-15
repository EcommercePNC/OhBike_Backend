package com.example.OhBike.service;

import com.example.OhBike.dto.request.ProductVariantRequestDTO;
import com.example.OhBike.dto.response.ProductVariantResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ProductVariantService {

    ProductVariantResponseDTO create(ProductVariantRequestDTO request);

    ProductVariantResponseDTO getById(UUID id);

    List<ProductVariantResponseDTO> getByProduct(UUID productId);

    List<ProductVariantResponseDTO> getLowStock();

    ProductVariantResponseDTO update(UUID id, ProductVariantRequestDTO request);

    void delete(UUID id);
}