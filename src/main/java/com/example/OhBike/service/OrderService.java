package com.example.OhBike.service;

import com.example.OhBike.dto.request.CheckoutRequest;
import com.example.OhBike.dto.request.OrderStatusRequest;
import com.example.OhBike.dto.response.CheckoutSummaryResponse;
import com.example.OhBike.dto.response.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    CheckoutSummaryResponse previewCheckout(CheckoutRequest request, String email); // GET preview antes de confirmar

    OrderResponse checkout(CheckoutRequest request, String email);                  // POST — crea la orden (PENDING)

    OrderResponse payOrder(UUID orderId, String email);                             // PATCH — PENDING → PAID

    OrderResponse shipOrder(UUID orderId);                            // PATCH — PAID → SHIPPED

    OrderResponse getById(UUID orderId);

    List<OrderResponse> getMyOrders(String email);

    List<OrderResponse> getAllOrders();                                // solo admin
}