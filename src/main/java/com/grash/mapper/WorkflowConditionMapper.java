package com.grash.mapper;

import com.grash.dto.WorkflowConditionPatchDTO;
import com.grash.model.WorkflowCondition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface WorkflowConditionMapper {
    WorkflowCondition updateWorkflowCondition(@MappingTarget WorkflowCondition entity, WorkflowConditionPatchDTO dto);

    @Mappings({})
    WorkflowConditionPatchDTO toPatchDto(WorkflowCondition model);
}
