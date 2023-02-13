package com.grash.dto;

import com.grash.model.*;
import com.grash.model.enums.Priority;
import com.grash.model.enums.WorkflowActionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowActionPostDTO {
    @NotNull
    private WorkflowActionEnum workflowActionEnum;
    private Priority priority;
    private Asset asset;
    private Location location;
    private OwnUser user;
    private Team team;
    private WorkOrderCategory category;
    private Checklist checklist;
}
