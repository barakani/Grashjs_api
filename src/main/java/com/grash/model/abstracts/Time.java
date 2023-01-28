package com.grash.model.abstracts;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class Time extends CompanyAudit {
    
    private long duration = 0;
}
