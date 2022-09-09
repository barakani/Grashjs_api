package com.grash.model.abstracts;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class Cost extends DateAudit {
    private double cost;
}
