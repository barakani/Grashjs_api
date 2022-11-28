package com.grash;

import com.grash.model.Company;
import com.grash.model.Role;
import com.grash.model.enums.RoleCode;
import com.grash.model.enums.RoleType;
import com.grash.service.CompanyService;
import com.grash.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;

@SpringBootApplication
@RequiredArgsConstructor
public class ApiApplication implements CommandLineRunner {

    @Value("${superAdmin.role.name}")
    private String superAdminRole;

    private final RoleService roleService;
    private final CompanyService companyService;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (!roleService.findByName(superAdminRole).isPresent()) {
            Company company = companyService.create(new Company());
            roleService.create(new Role(RoleType.ROLE_SUPER_ADMIN, superAdminRole, new HashSet<>(), company.getCompanySettings(), RoleCode.ADMIN));
        }
    }
}
