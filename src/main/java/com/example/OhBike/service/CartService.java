package com.example.OhBike.service;

import com.example.OhBike.dto.request.AddCartItemRequest;
import com.example.OhBike.dto.request.UpdateCartItemRequest;
import com.example.OhBike.dto.response.CartResponse;
import com.example.OhBike.dto.response.CartSummaryResponse;

import java.util.UUID;

public interface CartService {

    CartResponse getMyCart();                                                // GET  /cart/items
    CartResponse addItem(AddCartItemRequest request);                        // POST /cart/items
    CartResponse updateItem(UUID cartItemId, UpdateCartItemRequest request); // PUT  /cart/items/{id}
    CartResponse removeItem(UUID cartItemId);                                // DELETE /cart/items/{id}
    CartResponse clearCart();                                                // DELETE /cart/items
    CartSummaryResponse getSummary();                                        // GET  /cart/summary
}