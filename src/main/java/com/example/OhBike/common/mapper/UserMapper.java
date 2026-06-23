package com.example.OhBike.common.mapper;

import com.example.OhBike.dto.request.RegisterRequest;
import com.example.OhBike.dto.response.UserResponse;
import com.example.OhBike.entity.Role;
import com.example.OhBike.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request, Role role, String encodedPassword) {
        if (request == null) {
            return null;
        }

        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(role)
                .build();
    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }
}