package com.example.OhBike.service;

import com.example.OhBike.dto.request.CheckoutRequest;
import com.example.OhBike.dto.request.OrderStatusRequest;
import com.example.OhBike.dto.response.CheckoutSummaryResponse;
import com.example.OhBike.dto.response.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    CheckoutSummaryResponse previewCheckout(CheckoutRequest request); // GET preview antes de confirmar

    OrderResponse checkout(CheckoutRequest request);                  // POST — crea la orden (PENDING)

    OrderResponse payOrder(UUID orderId);                             // PATCH — PENDING → PAID

    OrderResponse shipOrder(UUID orderId);                            // PATCH — PAID → SHIPPED

    OrderResponse getById(UUID orderId);

    List<OrderResponse> getMyOrders();

    List<OrderResponse> getAllOrders();                                // solo admin
}