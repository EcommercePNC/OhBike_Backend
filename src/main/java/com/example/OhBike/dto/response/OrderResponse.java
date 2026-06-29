package com.example.OhBike.dto.response;

import com.example.OhBike.entity.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {

    private UUID orderId;
    private UUID userId;
    private String userName;
    private String paymentMethod;
    private String shippingMethod;
    private BigDecimal shippingCost;
    private String couponCode;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal total;
    private OrderStatus status;
    private List<OrderDetailResponse> details;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}