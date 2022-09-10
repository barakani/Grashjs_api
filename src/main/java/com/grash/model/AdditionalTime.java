package com.grash.model;

import com.grash.model.abstracts.Time;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
}
