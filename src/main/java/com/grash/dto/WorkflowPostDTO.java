package com.grash.dto;

import com.grash.model.enums.WFMainCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowPostDTO {
    @NotNull
    private WFMainCondition mainCondition;
    private List<WorkflowConditionPostDTO> secondaryConditions = new ArrayList<>();
    @NotNull
    private WorkflowActionPostDTO action;
}
