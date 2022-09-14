package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.abstracts.WorkOrderBase;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class PreventiveMaintenance extends WorkOrderBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Schedule schedule = new Schedule();

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;


}
