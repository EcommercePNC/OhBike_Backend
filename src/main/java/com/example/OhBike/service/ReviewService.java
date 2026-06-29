package com.example.OhBike.service;

import com.example.OhBike.dto.request.ReviewRequest;
import com.example.OhBike.dto.response.ReviewResponse;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    void createReview(String email, UUID productId, ReviewRequest request);
    void deleteReview(String email, UUID reviewId);
    List<ReviewResponse> getReviewsByProduct(UUID productId);
}