package com.grash.model.abstracts;

import com.grash.model.Company;
import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
public abstract class Cost extends Audit {

    @NotNull
    private double cost;

    @OneToOne
    private Company company;

}
