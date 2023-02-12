package com.grash.dto;

import com.grash.model.WorkflowAction;
import com.grash.model.WorkflowCondition;
import com.grash.model.enums.WFMainCondition;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WorkflowPatchDTO {
    private WFMainCondition mainCondition;
    private List<WorkflowCondition> secondaryConditions;
    private WorkflowAction action;
}
