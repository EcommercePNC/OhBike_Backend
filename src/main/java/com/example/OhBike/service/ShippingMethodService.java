package com.example.OhBike.service;

import com.example.OhBike.dto.request.ShippingMethodRequest;
import com.example.OhBike.dto.response.ShippingMethodResponse;

import java.util.List;
import java.util.UUID;

public interface ShippingMethodService {
    List<ShippingMethodResponse> getAllShippingMethods();
    ShippingMethodResponse getShippingMethodById(UUID id);
    ShippingMethodResponse createShippingMethod(ShippingMethodRequest request);
    ShippingMethodResponse updateShippingMethod(UUID id, ShippingMethodRequest request);
    void deleteShippingMethod(UUID id);
}
