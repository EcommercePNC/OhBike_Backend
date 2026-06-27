package com.example.OhBike.service;

import com.example.OhBike.dto.response.OrderDetailResponse;

import java.util.List;

public interface OrderItemService {

    List<OrderDetailResponse> getVendorOrderItems();

    List<OrderDetailResponse> getAdminOrderItems();

}