package com.example.OhBike.common.mapper;

import com.example.OhBike.dto.request.ShippingMethodRequest;
import com.example.OhBike.dto.response.ShippingMethodResponse;
import com.example.OhBike.entities.ShippingMethod;
import org.springframework.stereotype.Component;

@Component
public class ShippingMethodMapper {

    public ShippingMethod toEntity(ShippingMethodRequest request) {
        return ShippingMethod.builder()
                .name(request.name())
                .baseCost(request.baseCost())
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
