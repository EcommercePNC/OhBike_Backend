package com.example.OhBike.mapper;

import com.example.OhBike.dto.request.PaymentMethodRequest;
import com.example.OhBike.dto.response.PaymentMethodResponse;
import com.example.OhBike.entity.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMapper {

    public PaymentMethod toEntity(PaymentMethodRequest request) {
        return PaymentMethod.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public PaymentMethodResponse toDto(PaymentMethod entity) {
        return PaymentMethodResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
