package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.abstracts.Time;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class Labor extends Time {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User worker;

    @OneToOne(cascade = CascadeType.ALL)
    private LaborCost laborCost = new LaborCost();

    @ManyToOne
    @NotNull
    @JsonIgnore
    private WorkOrder workOrder;
}
