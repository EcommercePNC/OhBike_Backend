package com.example.OhBike.common.mapper;

import com.example.OhBike.dto.request.PaymentMethodRequest;
import com.example.OhBike.dto.response.PaymentMethodResponse;
import com.example.OhBike.entities.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMapper {

    public PaymentMethod toEntity(PaymentMethodRequest request) {
        return PaymentMethod.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public PaymentMethodResponse toDto(PaymentMethod entity) {
        return new PaymentMethodResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }
}
