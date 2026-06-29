package com.example.OhBike.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingMethodResponse {

    private UUID id;
    private String name;
    private BigDecimal baseCost;
}