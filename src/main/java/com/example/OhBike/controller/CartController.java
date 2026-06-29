package com.example.OhBike.controller;

import com.example.OhBike.dto.request.AddCartItemRequest;
import com.example.OhBike.dto.request.UpdateCartItemRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // GET /api/cart/items
    @GetMapping("/items")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<GeneralResponse> getMyCart(Authentication authentication) {
        String clientEmail = authentication.getName();
        return buildResponse("Cart retrieved successfully", HttpStatus.OK, cartService.getMyCart(clientEmail));
    }

    // POST /api/cart/items
    @PostMapping("/items")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<GeneralResponse> addItem(@Valid @RequestBody AddCartItemRequest request, Authentication authentication) {
        String clientEmail = authentication.getName();

        return buildResponse("Item added to cart", HttpStatus.OK, cartService.addItem(request, clientEmail));
    }

    // PUT /api/cart/items/{cartItemId}
    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<GeneralResponse> updateItem(
            @PathVariable UUID cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request,
            Authentication authentication) {
        String clientEmail = authentication.getName();
        return buildResponse("Cart item updated", HttpStatus.OK, cartService.updateItem(cartItemId, request, clientEmail));
    }

    // DELETE /api/cart/items/{cartItemId}
    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<GeneralResponse> removeItem(@PathVariable UUID cartItemId, Authentication authentication) {
        String clientEmail = authentication.getName();

        return buildResponse("Item removed from cart", HttpStatus.OK, cartService.removeItem(cartItemId, clientEmail));
    }

    // DELETE /api/cart/items
    @DeleteMapping("/items")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<GeneralResponse> clearCart(Authentication authentication) {
        String clientEmail = authentication.getName();

        return buildResponse("Cart cleared", HttpStatus.OK, cartService.clearCart(clientEmail));
    }

    // GET /api/cart/summary
    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<GeneralResponse> getSummary(Authentication authentication) {
        String clientEmail = authentication.getName();

        return buildResponse("Cart summary", HttpStatus.OK, cartService.getSummary(clientEmail));
    }

    // POST /api/cart/refresh
    @PostMapping("/refresh")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<GeneralResponse> refresh(Authentication authentication) {
        String clientEmail = authentication.getName();

        return buildResponse("Cart refreshed", HttpStatus.OK, cartService.refresh(clientEmail));
    }

    private ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity.status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(LocalDateTime.now())
                        .data(data)
                        .build());
    }
}