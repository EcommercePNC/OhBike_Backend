package com.example.OhBike.dto.request;

import com.example.OhBike.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequest {

    @NotNull(message = "Status is required")
    private OrderStatus status;
}