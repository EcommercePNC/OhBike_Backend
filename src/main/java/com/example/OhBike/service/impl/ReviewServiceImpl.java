package com.example.OhBike.service.impl;

import com.example.OhBike.entity.User;
import com.example.OhBike.entity.enums.OrderStatus;
import com.example.OhBike.exception.UnauthorizedOperationException;
import com.example.OhBike.mapper.UserMapper;
import com.example.OhBike.dto.request.ReviewRequest;
import com.example.OhBike.dto.response.ReviewResponse;
import com.example.OhBike.entity.Review;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.repository.OrderRepository;
import com.example.OhBike.repository.ProductRepository;
import com.example.OhBike.repository.ReviewRepository;
import com.example.OhBike.repository.UserRepository;
import com.example.OhBike.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserMapper userMapper;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void createReview(String email, UUID productId, ReviewRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (reviewRepository.existsByUser_IdAndProduct_Id(user.getId(), productId)) {
            throw new BusinessRuleException("You have already reviewed this product.");
        }

        boolean hasPurchased = orderRepository.existsByUserIdAndProductIdAndStatus(
                user.getId(),
                productId,
                OrderStatus.SHIPPED
        );

        if (!hasPurchased) {
            throw new BusinessRuleException("You can only review products you have purchased and received.");
        }

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void deleteReview(String email, UUID reviewId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        boolean isOwner = user.getId().equals(review.getUser().getId());
        boolean isAdmin = user.getRole().getName().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new UnauthorizedOperationException("You do not have permission to delete this review.");
        }
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public List<ReviewResponse> getReviewsByProduct(UUID productId) {
        return reviewRepository.findByProduct_Id(productId).stream()
                .map(review -> ReviewResponse.builder()
                        .reviewId(review.getReviewId())
                        .user(userMapper.toSummaryResponse(review.getUser()))
                        .productId(productId)
                        .rating(review.getRating())
                        .comment(review.getComment())
                        .createdAt(review.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}