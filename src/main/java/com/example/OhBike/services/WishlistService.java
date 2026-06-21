package com.example.OhBike.services;

import com.example.OhBike.dto.response.WishlistResponse;
import com.example.OhBike.repository.ProductRepository;
import com.example.OhBike.repository.UserRepository;
import com.example.OhBike.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<WishlistResponse> getWishlistByUser(UUID userId) {
        return wishlistRepository.findByUser_Id(userId)
                .stream()
                .map(wishlist -> WishlistResponse.builder()
                        .wishlistId(wishlist.getId())
                        .product(productMapper.toDto(wishlist.getProduct()))
                        .addedAt(wishlist.getCreatedAt())
                        .build())
                .toList();
    }
}
