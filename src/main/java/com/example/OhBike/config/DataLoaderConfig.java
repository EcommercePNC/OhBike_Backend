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
                System.out.println("Cargando roles por defecto en la base de datos...");

                Role cliente = Role.builder().name("CLIENTE").build();
                Role admin = Role.builder().name("ADMIN").build();
                Role vendedor = Role.builder().name("VENDEDOR").build();

                roleRepository.saveAll(List.of(cliente, admin, vendedor));

                System.out.println("¡Roles cargados exitosamente!");
            }
        };
    }
}