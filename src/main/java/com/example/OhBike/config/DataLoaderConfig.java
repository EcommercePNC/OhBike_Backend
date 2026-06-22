package com.example.OhBike.config;

import com.example.OhBike.entity.Role;
import com.example.OhBike.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoaderConfig {

    @Bean
    public CommandLineRunner loadData(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                System.out.println("loading roles...");

                Role client = Role.builder().name("CLIENT").build();
                Role admin = Role.builder().name("ADMIN").build();
                Role seller = Role.builder().name("SELLER").build();

                roleRepository.saveAll(List.of(client, admin, seller));

                System.out.println("roles loaded successfully.");
            }
        };
    }
}