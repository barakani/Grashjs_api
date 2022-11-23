package com.grash.model.abstracts;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
public abstract class Time extends CompanyAudit {

    @NotNull
    private int hours;
    @NotNull
    private int minutes;
}
