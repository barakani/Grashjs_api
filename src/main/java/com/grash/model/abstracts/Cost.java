package com.grash.model.abstracts;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
public abstract class Cost extends DateAudit {

    @NotNull
    private double cost;

}
