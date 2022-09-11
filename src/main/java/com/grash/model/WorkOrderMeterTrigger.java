package com.grash.model;

import com.grash.model.enums.WorkOrderMeterTriggerCondition;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class WorkOrderMeterTrigger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isOneTime;

    private Date date;

    @OneToOne
    private WorkOrder workOrder;

    private WorkOrderMeterTriggerCondition workOrderMeterTriggerCondition;

    private int value;

    private int waitBefore;

    @ManyToOne
    @NotNull
    private Meter meter;

}

