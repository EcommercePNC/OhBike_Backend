package com.example.OhBike.service.impl;

import com.example.OhBike.mapper.UserMapper;
import com.example.OhBike.dto.request.UserUpdateRequest;
import com.example.OhBike.dto.response.UserResponse;
import com.example.OhBike.entity.User;
import com.example.OhBike.exception.DuplicateFieldException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.repository.UserRepository;
import com.example.OhBike.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getMe(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateMe(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getPhone() != null && !request.getPhone().isBlank()) {

            if (!user.getPhone().equals(request.getPhone()) && userRepository.existsByPhone(request.getPhone())) {
                throw new DuplicateFieldException("phone", "phone number already registered");
            }
            user.setPhone(request.getPhone());
        }

        if (request.getAddress() != null && !request.getAddress().isBlank()) {
            user.setAddress(request.getAddress());
        }
        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }
}
