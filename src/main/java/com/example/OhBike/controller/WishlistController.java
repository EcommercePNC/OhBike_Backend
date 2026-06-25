package com.example.OhBike.controller;

import com.example.OhBike.dto.response.WishlistResponse;
import com.example.OhBike.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<WishlistResponse>> getWishlist(@PathVariable UUID userId) {
        return ResponseEntity.ok(wishlistService.getWishlistByUser(userId));
    }

    @PostMapping("/{userId}/product/{productId}")
    public ResponseEntity<Void> addProduct(@PathVariable UUID userId, @PathVariable UUID productId) {
        wishlistService.addProductToWishlist(userId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}/product/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable UUID userId, @PathVariable UUID productId) {
        wishlistService.removeProductFromWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }
}