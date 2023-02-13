package com.grash.dto;

import com.grash.model.*;
import com.grash.model.enums.Priority;
import com.grash.model.enums.WFSecondaryCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowConditionPostDTO {
    @NotNull
    private WFSecondaryCondition wfSecondaryCondition;
    private Priority priority;
    private Asset asset;
    private Location location;
    private OwnUser user;
    private Team team;
    private WorkOrderCategory category;
    private Checklist checklist;
    private Integer createdTimeStart;
    private Integer createdTimeEnd;
    private Date dueDateStart;
    private Date dueDateEnd;
}
