package com.example.OhBike.service.impl;

import com.example.OhBike.common.mapper.ProductMapper;
import com.example.OhBike.dto.response.WishlistResponse;
import com.example.OhBike.entity.Wishlist;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.repository.ProductRepository;
import com.example.OhBike.repository.UserRepository;
import com.example.OhBike.repository.WishlistRepository;
import com.example.OhBike.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<WishlistResponse> getWishlistByUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no ha sido encontrado con ID: " + userId);
        }

        return wishlistRepository.findByUser_Id(userId)
                .stream()
                .map(wishlist -> WishlistResponse.builder()
                        .wishlistId(wishlist.getId())
                        .product(productMapper.toDto(wishlist.getProduct()))
                        .addedAt(wishlist.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void addProductToWishlist(UUID userId, UUID productId) {
        if (wishlistRepository.existsByUser_IdAndProduct_Id(userId, productId)) {
            throw new BusinessRuleException("El producto ya existe en la whislist");
        }

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no ha sido encontrado"));

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no ha sido encontrado"));

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();

        wishlistRepository.save(wishlist);
    }

    @Override
    @Transactional
    public void removeProductFromWishlist(UUID userId, UUID productId) {
        if (!wishlistRepository.existsByUser_IdAndProduct_Id(userId, productId)) {
            throw new ResourceNotFoundException("No se encontró el producto en la wishlist del usuario");
        }

        wishlistRepository.deleteByUser_IdAndProduct_Id(userId, productId);
    }
}