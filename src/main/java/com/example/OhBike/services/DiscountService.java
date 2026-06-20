package com.example.OhBike.services;

import com.example.OhBike.dto.request.DiscountRequest;
import com.example.OhBike.dto.response.DiscountResponse;

import java.util.List;
import java.util.UUID;

public interface DiscountService {
    DiscountResponse createDiscount(DiscountRequest request);
    DiscountResponse updateDiscount(UUID id, DiscountRequest request);
    void deleteDiscount(UUID id);
    DiscountResponse getByIdDiscount(UUID id);
    List<DiscountResponse> findAllActiveDiscount();
}
