package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class SingleWorkOrderFieldConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private FieldConfiguration fieldConfiguration;
    private boolean forCreation;

    @ManyToOne
    @NotNull
    @JsonIgnore
    WorkOrderConfiguration workOrderConfiguration;

    public SingleWorkOrderFieldConfiguration(String fieldName, boolean forCreation, WorkOrderConfiguration workOrderConfiguration) {
        this.workOrderConfiguration = workOrderConfiguration;
        this.fieldConfiguration = new FieldConfiguration(fieldName);
        this.forCreation = forCreation;
    }

}
