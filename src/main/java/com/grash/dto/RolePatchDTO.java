package com.grash.dto;

import com.grash.model.enums.BasicPermission;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class RolePatchDTO {

    private String name;

    private String description;

    private String externalId;

    private Set<BasicPermission> permissions;

}
