package com.grash.model.abstracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.CompanySettings;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class CategoryAbstract extends Audit {

    @NotNull
    private String name;

    @ManyToOne
    @NotNull
    @JsonIgnore
    private CompanySettings companySettings;

    public CategoryAbstract(String name, CompanySettings companySettings) {
        this.name = name;
        this.companySettings = companySettings;
    }

}
