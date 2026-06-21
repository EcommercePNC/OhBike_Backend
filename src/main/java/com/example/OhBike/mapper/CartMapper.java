package com.example.OhBike.mapper;

import com.example.OhBike.dto.response.CartItemResponse;
import com.example.OhBike.dto.response.CartResponse;
import com.example.OhBike.entity.Cart;
import com.example.OhBike.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartItemResponse toItemDto(CartItem item) {
        BigDecimal subtotal = item.getVariant().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        return CartItemResponse.builder()
                .cartItemId(item.getId())
                .variantId(item.getVariant().getId())
                .productName(item.getVariant().getProduct().getName())
                .size(item.getVariant().getSize())
                .color(item.getVariant().getColor())
                .sku(item.getVariant().getSku())
                .unitPrice(item.getVariant().getPrice())
                .quantity(item.getQuantity())
                .subtotal(subtotal)
                .build();
    }

    public CartResponse toDto(Cart cart) {
        List<CartItemResponse> itemDtos = cart.getItems()
                .stream().map(this::toItemDto).toList();

        int totalItems = itemDtos.stream()
                .mapToInt(CartItemResponse::getQuantity).sum();

        BigDecimal totalPrice = itemDtos.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                .items(itemDtos)
                .totalItems(totalItems)
                .totalPrice(totalPrice)
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}