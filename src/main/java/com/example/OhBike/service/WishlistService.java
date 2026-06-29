package com.example.OhBike.service;

import com.example.OhBike.mapper.ProductMapper;
import com.example.OhBike.dto.response.WishlistResponse;

import java.util.List;
import java.util.UUID;

public interface WishlistService {
    List<WishlistResponse> getWishlistByUser(String email);
    void addProductToWishlist(String email, UUID productId);
    void removeProductFromWishlist(String email, UUID productId);
}