package com.grash.mapper;

import com.grash.dto.WorkOrderPatchDTO;
import com.grash.model.WorkOrder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface WorkOrderMapper {
    WorkOrder updateWorkOrder(@MappingTarget WorkOrder entity, WorkOrderPatchDTO dto);

    @Mappings({})
    WorkOrderPatchDTO toDto(WorkOrder model);
}
