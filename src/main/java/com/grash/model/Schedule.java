package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.abstracts.Audit;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Schedule extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean disabled;

    private Date startsOn;

    @NotNull
    private int frequency = 1;

    private Date endsOn;

    @OneToOne
    @JsonIgnore
    private PreventiveMaintenance preventiveMaintenance;

    public Schedule(PreventiveMaintenance preventiveMaintenance) {
        this.preventiveMaintenance = preventiveMaintenance;
    }

}
