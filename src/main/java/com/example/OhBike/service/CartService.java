package com.example.OhBike.service;

import com.example.OhBike.dto.request.AddCartItemRequest;
import com.example.OhBike.dto.request.UpdateCartItemRequest;
import com.example.OhBike.dto.response.CartResponse;

import java.util.UUID;

public interface CartService {

    //Aqui dejé marcados a cuales endpoints se estan usando para no perderme
    CartResponse getMyCart();                                          // GET  /cart/items

    CartResponse addItem(AddCartItemRequest request);                  // POST /cart/items

    CartResponse updateItem(UUID cartItemId, UpdateCartItemRequest request); // PUT /cart/items/{cartItemId}

    CartResponse removeItem(UUID cartItemId);                          // DELETE /cart/items/{cartItemId}

    CartResponse clearCart();                                          // DELETE /cart/items
}