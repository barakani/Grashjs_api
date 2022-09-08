package com.graphjs.model;
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
public class WorkOrderMetricTrigger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isOneTime;

    private Date date;

//    private WorkOrder workOrder;

//    private WorkOrderMeterTriggerCondition condition;

    private int value;

    private int waitBefore;

}

