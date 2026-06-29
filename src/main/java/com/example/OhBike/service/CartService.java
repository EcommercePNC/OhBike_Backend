package com.example.OhBike.service;

import com.example.OhBike.dto.request.AddCartItemRequest;
import com.example.OhBike.dto.request.UpdateCartItemRequest;
import com.example.OhBike.dto.response.CartRefreshResponse;
import com.example.OhBike.dto.response.CartResponse;
import com.example.OhBike.dto.response.CartSummaryResponse;

import java.util.UUID;

public interface CartService {

    CartResponse getMyCart(String sellerEmail);                                                // GET  /cart/items
    CartResponse addItem(AddCartItemRequest request, String sellerEmail);                        // POST /cart/items
    CartResponse updateItem(UUID cartItemId, UpdateCartItemRequest request, String sellerEmail); // PUT  /cart/items/{id}
    CartResponse removeItem(UUID cartItemId, String sellerEmail);                                // DELETE /cart/items/{id}
    CartResponse clearCart(String sellerEmail);                                                // DELETE /cart/items
    CartSummaryResponse getSummary(String sellerEmail);                                        // GET  /cart/summary
    CartRefreshResponse refresh(String sellerEmail);                                           // POST /cart/refresh
}