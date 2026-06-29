package com.example.OhBike.service;

import com.example.OhBike.dto.response.ProductAvailabilityResponse;

import java.util.UUID;

public interface InventoryService {
    ProductAvailabilityResponse getAvailability(UUID productId);
}