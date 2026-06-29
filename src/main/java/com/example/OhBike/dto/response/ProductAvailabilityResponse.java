package com.example.OhBike.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductAvailabilityResponse {
    private boolean available;
    private int stock;
}