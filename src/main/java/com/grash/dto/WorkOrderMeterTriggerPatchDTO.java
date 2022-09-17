package com.grash.dto;

import com.grash.model.enums.WorkOrderMeterTriggerCondition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class WorkOrderMeterTriggerPatchDTO {
    private boolean isOneTime;

    private Date date;

    private WorkOrderMeterTriggerCondition workOrderMeterTriggerCondition;

    private int value;

    private int waitBefore;

}
