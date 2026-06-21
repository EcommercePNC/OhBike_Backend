package com.example.OhBike.common.mapper;

import com.example.OhBike.dto.response.WishlistResponse;
import com.example.OhBike.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface WishlistMapper {

    @Mapping(source = "id", target = "wishlistId")
    @Mapping(source = "createdAt", target = "addedAt")
    WishlistResponse toDto(Wishlist wishlist);
}