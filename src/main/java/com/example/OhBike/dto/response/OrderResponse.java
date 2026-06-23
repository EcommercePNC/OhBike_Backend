package com.example.OhBike.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private UUID id;
    private UUID userId;
    private String userName;
    private String couponCode;
    private UUID shippingMethodId;
    private LocalDateTime orderDate;
    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    private BigDecimal discountTotal;
    private String status;
    private BigDecimal total;
    private List<OrderDetailResponse> items;
}