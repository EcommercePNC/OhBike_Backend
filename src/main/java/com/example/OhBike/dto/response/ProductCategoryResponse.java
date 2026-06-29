package com.example.OhBike.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductCategoryResponse {
    private UUID id;
    private String name;
}
