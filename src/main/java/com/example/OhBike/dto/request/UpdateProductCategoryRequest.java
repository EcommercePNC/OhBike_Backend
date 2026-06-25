package com.example.OhBike.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductCategoryRequest {
    @NotBlank(message = "Category name cannot be empty")
    private String name;
}
