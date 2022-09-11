package com.grash.model;

import com.grash.model.enums.BasicPermission;
import com.grash.model.enums.RoleType;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private RoleType roleType;

    private String name;

    @ElementCollection(targetClass = BasicPermission.class)
    private Set<BasicPermission> permissions;

    @ManyToOne
    @NotNull
    private CompanySettings companySettings;

    public Role(RoleType roleType, String name, HashSet<BasicPermission> basicPermissions, CompanySettings companySettings) {
        this.name = name;
        this.roleType = roleType;
        this.companySettings = companySettings;
        this.permissions = basicPermissions;
    }
}
