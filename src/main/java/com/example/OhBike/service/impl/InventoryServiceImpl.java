package com.example.OhBike.service.impl;

import com.example.OhBike.dto.response.ProductAvailabilityResponse;
import com.example.OhBike.entity.ProductVariant;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.repository.ProductRepository;
import com.example.OhBike.repository.ProductVariantRepository;
import com.example.OhBike.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    @Override
    public ProductAvailabilityResponse getAvailability(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        List<ProductVariant> activeVariants =
                variantRepository.findByProductIdAndActiveTrue(productId);

        int totalStock = activeVariants.stream()
                .mapToInt(ProductVariant::getStock)
                .sum();

        return ProductAvailabilityResponse.builder()
                .available(totalStock > 0)
                .stock(totalStock)
                .build();
    }
}