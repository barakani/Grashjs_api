package com.grash;

import com.grash.model.Role;
import com.grash.model.enums.RoleType;
import com.grash.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class ApiApplication implements CommandLineRunner {

    @Value("${superAdmin.role.name}")
    private String superAdminRole;

    private final RoleService roleService;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) throws Exception {
        if (!roleService.findByName(superAdminRole).isPresent()) {
            roleService.create(new Role(RoleType.ROLE_SUPER_ADMIN, superAdminRole));
        }
    }
}
