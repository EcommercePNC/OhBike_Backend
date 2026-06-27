package com.example.OhBike.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReviewResponse {
    private UUID reviewId;
    private UserSummaryResponse user;
    private UUID productId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}