package com.example.OhBike.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class WishlistResponse {
    private UUID wishlistId;
    private ProductResponse product;
    private LocalDateTime addedAt;
}
