package com.example.OhBike.service.impl;

import com.example.OhBike.mapper.UserMapper;
import com.example.OhBike.dto.request.RoleUpdateRequest;
import com.example.OhBike.dto.response.UserResponse;
import com.example.OhBike.entity.Role;
import com.example.OhBike.entity.User;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.repository.RoleRepository;
import com.example.OhBike.repository.UserRepository;
import com.example.OhBike.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse updateUserRole(UUID userId, RoleUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String requestedRole = request.getRoleName().toUpperCase();

        if(requestedRole.equals("ADMIN")) {
            throw new BusinessRuleException("Cannot assign ADMIN role");
        }
        if(requestedRole.equals(user.getRole().getName())) {
            throw new BusinessRuleException("User already has this role");
        }

        Role newRole = roleRepository.findByName(requestedRole)
                .orElseThrow(() -> new ResourceNotFoundException("Rol'" + requestedRole + "' does not exist"));

        user.setRole(newRole);
        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }
}
