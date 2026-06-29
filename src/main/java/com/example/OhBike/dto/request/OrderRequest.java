package com.example.OhBike.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

        private UUID couponId;

        @NotNull(message = "The shipping method ID is required")
        private UUID shippingMethodId;

        @NotEmpty(message = "The order must contain at least one item in the details")
        private List<OrderDetailRequest> items;
}