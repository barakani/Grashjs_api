package com.grash.dto;

import com.grash.model.*;
import com.grash.model.enums.Priority;
import com.grash.model.enums.WorkflowActionEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkflowActionPatchDTO {
    private WorkflowActionEnum workflowActionEnum;
    private Priority priority;
    private Asset asset;
    private Location location;
    private OwnUser user;
    private Team team;
    private WorkOrderCategory category;
    private Checklist checklist;
    private Workflow workflow;
    private Integer createdTimeStart;
    private Integer createdTimeEnd;
    private Integer dueDateStart;
    private Integer dueDateEnd;
}
