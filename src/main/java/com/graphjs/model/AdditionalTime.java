package com.graphjs.model;
import com.graphjs.model.abstracts.Cost;
import com.graphjs.model.abstracts.Time;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class AdditionalTime extends Time {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //private User assignedTo;

    private boolean includeToTotalTime;

    private double hourlyRate;

    private Date startedAt;

    //private TimeCategory timeCategory;
}
