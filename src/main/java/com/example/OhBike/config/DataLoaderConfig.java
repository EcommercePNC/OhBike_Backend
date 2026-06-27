package com.example.OhBike.config;

import com.example.OhBike.entity.Role;
import com.example.OhBike.entity.User;
import com.example.OhBike.repository.RoleRepository;
import com.example.OhBike.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataLoaderConfig {

    @Bean
    public CommandLineRunner loadData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepository.count() == 0) {
                System.out.println("loading roles...");

                Role client = Role.builder().name("CLIENT").build();
                Role admin = Role.builder().name("ADMIN").build();
                Role seller = Role.builder().name("SELLER").build();

                roleRepository.saveAll(List.of(client, admin, seller));

                System.out.println("roles loaded successfully.");
            }

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Error: ADMIN role not found."));

            if (!userRepository.existsByRole(adminRole)) {
                System.out.println("Creating unique Administrator user...");

                User superAdmin = User.builder()
                        .name("Im Admin")
                        .email("admin@ohbike.com")
                        .password(passwordEncoder.encode("OhBike2026"))
                        .phone("0000-0000")
                        .address("Oficina Central Oh Bike")
                        .role(adminRole)
                        .build();

                userRepository.save(superAdmin);
                System.out.println("Administrator created successfully.");
            }
        };
    }
}