package com.example.OhBike.controller;

import com.example.OhBike.dto.response.WishlistResponse;
import com.example.OhBike.service.WishlistService;
import com.example.OhBike.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
@PreAuthorize( "hasAuthority('CLIENT')")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping("/all") // Client (Only their own wishlist)
    public ResponseEntity<List<WishlistResponse>> getWishlist(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(wishlistService.getWishlistByUser(email));
    }

    @PostMapping("/product/{productId}") // Client (Only their own wishlist)
    public ResponseEntity<Void> addProduct(@PathVariable UUID productId, Authentication authentication) {
        String email = authentication.getName();
        wishlistService.addProductToWishlist(email, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/product/{productId}") // Client (Only their own wishlist)
    public ResponseEntity<Void> removeProduct(@PathVariable UUID productId, Authentication authentication) {
    String email = authentication.getName();
    wishlistService.removeProductFromWishlist(email, productId);
        return ResponseEntity.noContent().build();
    }
}