package com.grash.model.abstracts;

import com.grash.model.CompanySettings;
import lombok.Data;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
public abstract class CategoryAbstract extends Audit {
    private String name;

    @ManyToOne
    @NotNull
    private CompanySettings companySettings;
}
