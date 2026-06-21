package com.example.OhBike.service.impl;

import com.example.OhBike.dto.request.AddCartItemRequest;
import com.example.OhBike.dto.request.UpdateCartItemRequest;
import com.example.OhBike.dto.response.CartResponse;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository variantRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    // GET /cart/items

    @Override
    public CartResponse getMyCart() {
        Cart cart = getOrCreateCart();
        return cartMapper.toDto(cart);
    }

    // POST /cart/items

    @Override
    @Transactional
    public CartResponse addItem(AddCartItemRequest request) {
        Cart cart = getOrCreateCart();

        ProductVariant variant = findActiveVariant(request.getVariantId());

        if (variant.getStock() < request.getQuantity()) {
            throw new BusinessRuleException(
                    "Insufficient stock. Available: " + variant.getStock());
        }

        cartItemRepository.findByCart_IdAndVariant_Id(cart.getId(), variant.getId())
                .ifPresentOrElse(existing -> {
                    int newQty = existing.getQuantity() + request.getQuantity();
                    if (variant.getStock() < newQty) {
                        throw new BusinessRuleException(
                                "Insufficient stock for requested total quantity. Available: " + variant.getStock());
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

    // PUT /cart/items/{cartItemId}

    @Override
    @Transactional
    public CartResponse updateItem(UUID cartItemId, UpdateCartItemRequest request) {
        CartItem item = findCartItemOfCurrentUser(cartItemId);

        if (request.getQuantity() == 0) {
            cartItemRepository.delete(item);
        } else {
            if (item.getVariant().getStock() < request.getQuantity()) {
                throw new BusinessRuleException(
                        "Insufficient stock. Available: " + item.getVariant().getStock());
            }
            item.setQuantity(request.getQuantity());
            cartItemRepository.save(item);
        }

        return cartMapper.toDto(refreshCart(item.getCart().getId()));
    }

    // DELETE /cart/items/{cartItemId}

    @Override
    @Transactional
    public CartResponse removeItem(UUID cartItemId) {
        CartItem item = findCartItemOfCurrentUser(cartItemId);
        UUID cartId = item.getCart().getId();
        cartItemRepository.delete(item);
        return cartMapper.toDto(refreshCart(cartId));
    }

    // DELETE /cart/items

    @Override
    @Transactional
    public CartResponse clearCart() {
        Cart cart = getOrCreateCart();
        cart.getItems().clear();
        cartRepository.save(cart);
        return cartMapper.toDto(refreshCart(cart.getId()));
    }

    // Busca el carrito del usuario actual y si no existe, entonces crea uno nuevo
    private Cart getOrCreateCart() {
        UUID userId = AuthUtil.getCurrentUserId();

        return cartRepository.findByUser_Id(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        });
    }

    // Recarga el carrito desde BD para tener los items frescos tras modifcaciones.
    private Cart refreshCart(UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    // Busca un CartItem y verifica que pertenezca al usuario autenticado
    private CartItem findCartItemOfCurrentUser(UUID cartItemId) {
        UUID userId = AuthUtil.getCurrentUserId();
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!item.getCart().getUser().getId().equals(userId)) {
            throw new BusinessRuleException("This cart item does not belong to the current user");
        }
        return item;
    }

    //
    private ProductVariant findActiveVariant(UUID variantId) {
        return variantRepository.findById(variantId)
                .filter(ProductVariant::getActive)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found with id: " + variantId));
    }
}