package com.grash.dto;

import com.grash.model.CompanySettings;
import com.grash.model.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RolePatchDTO {

    private RoleType roleType;

    private String name;

    private CompanySettings companySettings;
}
