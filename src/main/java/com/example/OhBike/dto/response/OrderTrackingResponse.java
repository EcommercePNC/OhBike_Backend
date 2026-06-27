package com.example.OhBike.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderTrackingResponse {
    private String status;
    private String guide;
    private String carrier;
}