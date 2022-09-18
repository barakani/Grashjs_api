package com.grash.dto;

import com.grash.model.CompanySettings;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeterCategoryPatchDTO {

    private String name;

    private CompanySettings companySettings;
}
