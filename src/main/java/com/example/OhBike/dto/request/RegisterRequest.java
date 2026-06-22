package com.example.OhBike.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Name must not be blank")
    private String name;
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid email address")
    private String email;
    @NotBlank(message = "Password must not be blank")
    private String password;
    @NotBlank(message = "Phone must not be blank")
    private String phone;
    @NotBlank(message = "Address must not be blank")
    private String address;
}
