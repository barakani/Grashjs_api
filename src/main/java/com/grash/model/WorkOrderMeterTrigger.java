package com.grash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grash.model.enums.WorkOrderMeterTriggerCondition;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class WorkOrderMeterTrigger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isOneTime;

    @NotNull
    private Date date;

    @OneToOne
    private WorkOrder workOrder;

    private WorkOrderMeterTriggerCondition workOrderMeterTriggerCondition;

    private int value;

    @NotNull
    private int waitBefore;

    @ManyToOne
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Meter meter;

}

