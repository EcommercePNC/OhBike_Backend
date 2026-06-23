package com.example.OhBike.common.mapper;

import com.example.OhBike.dto.request.ShippingMethodRequest;
import com.example.OhBike.dto.response.ShippingMethodResponse;
import com.example.OhBike.entity.ShippingMethod;
import org.springframework.stereotype.Component;

@Component
public class ShippingMethodMapper {

    public ShippingMethod toEntity(ShippingMethodRequest request) {
        return ShippingMethod.builder()
                .name(request.getName())
                .baseCost(request.getBaseCost())
                .build();
    }

    public ShippingMethodResponse toDto(ShippingMethod entity) {
        return new ShippingMethodResponse(
                entity.getId(),
                entity.getName(),
                entity.getBaseCost()
        );
    }
}
