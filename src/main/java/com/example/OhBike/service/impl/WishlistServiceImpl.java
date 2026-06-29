package com.example.OhBike.service.impl;

import com.example.OhBike.dto.response.WishlistResponse;
import com.example.OhBike.entity.User;
import com.example.OhBike.entity.Wishlist;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.mapper.WishlistMapper;
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
    private final WishlistMapper wishlistMapper;

    @Override
    public List<WishlistResponse> getWishlistByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return wishlistRepository.findByUser_Id(user.getId())
                .stream()
                .map(wishlistMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void addProductToWishlist(String email, UUID productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (wishlistRepository.existsByUser_IdAndProduct_Id(user.getId(), productId)) {
            throw new BusinessRuleException("Product already exists in the wishlist");
        }

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();

        wishlistRepository.save(wishlist);
    }

    @Override
    @Transactional
    public void removeProductFromWishlist(String email, UUID productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!wishlistRepository.existsByUser_IdAndProduct_Id(user.getId(), productId)) {
            throw new ResourceNotFoundException("Product not found in the user's wishlist");
        }

        wishlistRepository.deleteByUser_IdAndProduct_Id(user.getId(), productId);
    }
}