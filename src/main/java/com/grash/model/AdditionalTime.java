package com.grash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grash.model.abstracts.Time;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class AdditionalTime extends Time {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User assignedTo;

    private boolean includeToTotalTime;

    private double hourlyRate;

    private Date startedAt;

    @OneToOne
    private TimeCategory timeCategory;

    @ManyToOne
    @JsonIgnore
    @NotNull
    private WorkOrder workOrder;
}
