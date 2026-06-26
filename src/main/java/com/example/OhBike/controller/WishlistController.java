package com.example.OhBike.controller;

import com.example.OhBike.dto.response.WishlistResponse;
import com.example.OhBike.service.WishlistService;
import com.example.OhBike.util.AuthUtil;
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

    @GetMapping("/all") // Client (Only their own wishlist)
    public ResponseEntity<List<WishlistResponse>> getWishlist() {
        UUID userId = AuthUtil.getCurrentUserId();
        return ResponseEntity.ok(wishlistService.getWishlistByUser(userId));
    }

    @PostMapping("/product/{productId}") // Client (Only their own wishlist)
    public ResponseEntity<Void> addProduct(@PathVariable UUID productId) {
        UUID userId = AuthUtil.getCurrentUserId();
        wishlistService.addProductToWishlist(userId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/product/{productId}") // Client (Only their own wishlist)
    public ResponseEntity<Void> removeProduct(@PathVariable UUID productId) {
        UUID userId = AuthUtil.getCurrentUserId();
        wishlistService.removeProductFromWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }
}