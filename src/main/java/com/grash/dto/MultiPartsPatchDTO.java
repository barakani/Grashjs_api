package com.grash.dto;

import com.grash.model.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MultiPartsPatchDTO {

    private String name;

    private Company company;
}
