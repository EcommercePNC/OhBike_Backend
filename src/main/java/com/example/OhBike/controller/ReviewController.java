package com.example.OhBike.controller;

import com.example.OhBike.dto.request.ReviewRequest;
import com.example.OhBike.dto.response.ReviewResponse;
import com.example.OhBike.service.ReviewService;
import com.example.OhBike.util.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/product/{productId}")// Client (Logged in)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> createReview(@PathVariable UUID productId,
                                             @Valid @RequestBody ReviewRequest request,
                                             Authentication authentication) {
        String email = authentication.getName();
        reviewService.createReview(email, productId, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{reviewId}") // Client (Owner of the review), Admin
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId,
                                             Authentication authentication) {
        String email = authentication.getName();

        reviewService.deleteReview(email, reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/product/{productId}")// Public (Admin, Seller, Client, Guest)
    public ResponseEntity<List<ReviewResponse>> getReviewsByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }
}