package com.example.OhBike.service.impl;

import com.example.OhBike.dto.request.AddCartItemRequest;
import com.example.OhBike.dto.request.UpdateCartItemRequest;
import com.example.OhBike.dto.response.CartItemResponse;
import com.example.OhBike.dto.response.CartRefreshResponse;
import com.example.OhBike.dto.response.CartResponse;
import com.example.OhBike.dto.response.CartSummaryResponse;
import com.example.OhBike.entity.Cart;
import com.example.OhBike.entity.CartItem;
import com.example.OhBike.entity.ProductVariant;
import com.example.OhBike.entity.User;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.mapper.CartMapper;
import com.example.OhBike.repository.CartItemRepository;
import com.example.OhBike.repository.CartRepository;
import com.example.OhBike.repository.ProductVariantRepository;
import com.example.OhBike.repository.UserRepository;
import com.example.OhBike.service.CartService;
import com.example.OhBike.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository variantRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    public CartResponse getMyCart(String email) {
        Cart cart = getOrCreateCart(email);
        return cartMapper.toDto(cart);

    }

    @Override
    @Transactional
    public CartResponse addItem(AddCartItemRequest request, String email) {
        Cart cart = getOrCreateCart(email);
        ProductVariant variant = findActiveVariant(request.getVariantId());

        if (variant.getStock() < request.getQuantity()) {
            throw new BusinessRuleException(
                    "PRODUCT_OUT_OF_STOCK",
                    "Insufficient stock. Available: " + variant.getStock());
        }

        cartItemRepository.findByCart_IdAndVariant_Id(cart.getId(), variant.getId())
                .ifPresentOrElse(existing -> {
                    int newQty = existing.getQuantity() + request.getQuantity();
                    if (variant.getStock() < newQty) {
                        throw new BusinessRuleException(
                                "PRODUCT_OUT_OF_STOCK",
                                "Insufficient stock for total quantity. Available: " + variant.getStock());
                    }
                    existing.setQuantity(newQty);
                    cartItemRepository.save(existing);
                }, () -> {
                    CartItem newItem = CartItem.builder()
                            .cart(cart)
                            .variant(variant)
                            .quantity(request.getQuantity())
                            .build();
                    cartItemRepository.save(newItem);
                });

        return cartMapper.toDto(refreshCart(cart.getId()));
    }

    @Override
    @Transactional
    public CartResponse updateItem(UUID cartItemId, UpdateCartItemRequest request, String email) {
        CartItem item = findCartItemOfCurrentUser(cartItemId, email);

        if (request.getQuantity() == 0) {
            cartItemRepository.delete(item);
        } else {
            if (item.getVariant().getStock() < request.getQuantity()) {
                throw new BusinessRuleException(
                        "PRODUCT_OUT_OF_STOCK",
                        "Insufficient stock. Available: " + item.getVariant().getStock());
            }
            item.setQuantity(request.getQuantity());
            cartItemRepository.save(item);
        }

        return cartMapper.toDto(refreshCart(item.getCart().getId()));
    }

    @Override
    @Transactional
    public CartResponse removeItem(UUID cartItemId, String email) {
        CartItem item = findCartItemOfCurrentUser(cartItemId, email);
        UUID cartId = item.getCart().getId();
        cartItemRepository.delete(item);
        return cartMapper.toDto(refreshCart(cartId));
    }

    @Override
    @Transactional
    public CartResponse clearCart(String email) {
        Cart cart = getOrCreateCart(email);
        cart.getItems().clear();
        cartRepository.save(cart);
        return cartMapper.toDto(refreshCart(cart.getId()));
    }

    @Override
    public CartSummaryResponse getSummary(String email) {
        Cart cart = getOrCreateCart(email);

        BigDecimal subtotal = cart.getItems().stream()
                .map(item -> item.getVariant().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity).sum();

        BigDecimal shipping = subtotal.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : new BigDecimal("5.00");

        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal total = subtotal.add(shipping).subtract(discount);

        return CartSummaryResponse.builder()
                .subtotal(subtotal)
                .discount(discount)
                .shipping(shipping)
                .total(total)
                .totalItems(totalItems)
                .build();
    }

    @Override
    @Transactional
    public CartRefreshResponse refresh(String email) {
        Cart cart = getOrCreateCart(email);

        List<CartItemResponse> validItems = new ArrayList<>();
        List<String> removedItems = new ArrayList<>();
        List<String> adjustedItems = new ArrayList<>();
        boolean wasModified = false;

        List<CartItem> toDelete = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            ProductVariant variant = item.getVariant();
            String sku = variant.getSku();

            if (!variant.getActive()) {
                toDelete.add(item);
                removedItems.add(sku + " (variant no longer available)");
                wasModified = true;
                continue;
            }

            int available = variant.getStock();

            if (available == 0) {
                toDelete.add(item);
                removedItems.add(sku + " (out of stock)");
                wasModified = true;
                continue;
            }

            if (available < item.getQuantity()) {
                item.setQuantity(available);
                cartItemRepository.save(item);
                adjustedItems.add(sku + " adjusted to " + available);
                wasModified = true;
            }

            BigDecimal subtotal = variant.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            validItems.add(CartItemResponse.builder()
                    .cartItemId(item.getId())
                    .variantId(variant.getId())
                    .productName(variant.getProduct().getName())
                    .size(variant.getSize())
                    .color(variant.getColor())
                    .sku(sku)
                    .unitPrice(variant.getPrice())
                    .quantity(item.getQuantity())
                    .subtotal(subtotal)
                    .build());
        }

        if (!toDelete.isEmpty()) {
            cartItemRepository.deleteAll(toDelete);
        }

        BigDecimal newSubtotal = validItems.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = validItems.stream()
                .mapToInt(CartItemResponse::getQuantity).sum();

        return CartRefreshResponse.builder()
                .items(validItems)
                .removedItems(removedItems)
                .adjustedItems(adjustedItems)
                .subtotal(newSubtotal)
                .totalItems(totalItems)
                .wasModified(wasModified)
                .build();
    }


    private Cart getOrCreateCart(String email) {
        User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        return cartRepository.findByUser_Id(user.getId()).orElseGet(() -> {
            return cartRepository.save(Cart.builder().user(user).build());
        });
    }

    private Cart refreshCart(UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    private CartItem findCartItemOfCurrentUser(UUID cartItemId, String email) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartItemId));
        if (!item.getCart().getUser().getEmail().equals(email)) {
            throw new BusinessRuleException("ACCESS_DENIED",
                    "This cart item does not belong to the current user.");
        }
        return item;
    }

    private ProductVariant findActiveVariant(UUID variantId) {
        return variantRepository.findById(variantId)
                .filter(ProductVariant::getActive)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found: " + variantId));
    }
}