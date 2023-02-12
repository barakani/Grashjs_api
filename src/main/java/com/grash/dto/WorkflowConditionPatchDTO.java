package com.grash.dto;

import com.grash.model.*;
import com.grash.model.enums.Priority;
import com.grash.model.enums.WFSecondaryCondition;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkflowConditionPatchDTO {
    private WFSecondaryCondition wfSecondaryCondition;
    private Priority priority;
    private Asset asset;
    private Location location;
    private OwnUser user;
    private Team team;
    private WorkOrderCategory category;
    private Workflow workflow;
    private Integer createdTimeStart;
    private Integer createdTimeEnd;
    private Integer dueDateStart;
    private Integer dueDateEnd;
}