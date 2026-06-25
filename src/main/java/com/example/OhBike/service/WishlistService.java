package com.example.OhBike.service;

import com.example.OhBike.mapper.ProductMapper;
import com.example.OhBike.dto.response.WishlistResponse;

import java.util.List;
import java.util.UUID;

public interface WishlistService {
    List<WishlistResponse> getWishlistByUser(UUID userId);
    void addProductToWishlist(UUID userId, UUID productId);
    void removeProductFromWishlist(UUID userId, UUID productId);
}