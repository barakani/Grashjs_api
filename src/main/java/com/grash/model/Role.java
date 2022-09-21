package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.enums.BasicPermission;
import com.grash.model.enums.RoleType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "companySettings")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private RoleType roleType;

    @NotNull
    private String name;

    @ElementCollection(targetClass = BasicPermission.class)
    private Collection<BasicPermission> permissions;

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CompanySettings companySettings;
    
    public Role(RoleType roleType, String name, HashSet<BasicPermission> basicPermissions, CompanySettings companySettings) {
        this.name = name;
        this.roleType = roleType;
        this.companySettings = companySettings;
        this.permissions = basicPermissions;
    }
}
