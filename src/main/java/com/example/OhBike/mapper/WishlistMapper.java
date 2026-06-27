package com.example.OhBike.mapper;

import com.example.OhBike.dto.response.WishlistResponse;
import com.example.OhBike.entity.Wishlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WishlistMapper {

    private final ProductMapper productMapper;

    public WishlistResponse toDto(Wishlist wishlist) {
        if (wishlist == null) return null;

        return WishlistResponse.builder()
                .wishlistId(wishlist.getId())
                .product(productMapper.toDto(wishlist.getProduct()))
                .addedAt(wishlist.getCreatedAt())
                .build();
    }
}