package com.grash.model;

import com.grash.model.enums.BasicPermission;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class PermissionsWrapper {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ElementCollection(targetClass = BasicPermission.class)
    private Collection<BasicPermission> permissions;
}
