package com.example.OhBike.services;

import com.example.OhBike.dto.request.DiscountRequest;
import com.example.OhBike.dto.response.DiscountResponse;
import com.example.OhBike.dto.response.GeneralResponse;
import java.util.List;
import java.util.UUID;

public interface DiscountService {
    GeneralResponse createDiscount(DiscountRequest request);
    GeneralResponse updateDiscount(UUID id, DiscountRequest request);
    GeneralResponse deleteDiscount(UUID id);
    DiscountResponse getByIdDiscount(UUID id);
    List<DiscountResponse> findAllActiveDiscount();
}