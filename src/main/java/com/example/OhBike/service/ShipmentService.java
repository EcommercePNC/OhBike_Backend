package com.example.OhBike.service;

import com.example.OhBike.dto.response.OrderTrackingResponse;

import java.util.UUID;

public interface ShipmentService {
    OrderTrackingResponse getTracking(UUID orderId);
}