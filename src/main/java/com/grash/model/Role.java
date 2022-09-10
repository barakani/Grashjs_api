package com.grash.model;

import com.grash.model.enums.BasicPermission;
import com.grash.model.enums.RoleType;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private RoleType roleType;

    @ElementCollection(targetClass = BasicPermission.class)
    private Set<BasicPermission> permissions;

    @ManyToOne
    @NotNull
    private CompanySettings companySettings;
}
