package com.grash.model.abstracts;

import com.grash.model.WorkOrder;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class Cost extends DateAudit {
    private double cost;

}
