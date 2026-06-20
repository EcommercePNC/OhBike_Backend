package com.example.OhBike.mapper;

import com.example.OhBike.dto.request.DiscountRequest;
import com.example.OhBike.dto.response.DiscountResponse;
import com.example.OhBike.entities.Discount;
import org.springframework.stereotype.Component;

@Component
public class DiscountMapper {

    public Discount toEntity(DiscountRequest request) {
        return Discount.builder()
                .name(request.getName())
                .discountType(request.getDiscountType())
                .value(request.getValue())
                .active(request.getActive())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }
    public DiscountResponse toDto(Discount entity) {
        return DiscountResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .discountType(entity.getDiscountType())
                .value(entity.getValue() != null ? entity.getValue().floatValue() : 0.0f)
                .active(entity.getActive())
                .build();
    }

    public void updateEntity(Discount entity, DiscountRequest request) {
        entity.setName(request.getName());
        entity.setDiscountType(request.getDiscountType());
        entity.setValue(request.getValue());
        entity.setActive(request.getActive());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
    }
}