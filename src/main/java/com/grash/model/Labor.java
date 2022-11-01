package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ManyToOne
    private OwnUser worker;

    @OneToOne(cascade = CascadeType.ALL)
    private LaborCost laborCost = new LaborCost(this);

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private WorkOrder workOrder;
}
