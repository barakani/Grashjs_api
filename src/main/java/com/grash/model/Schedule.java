package com.grash.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Date startsOn;

    @NotNull
    private int frequency;

    @NotNull
    private Date endsOn;

    @OneToOne(cascade = CascadeType.ALL)
    private PreventiveMaintenance preventiveMaintenance;
}
