package com.grash.mapper;

import com.grash.dto.WorkOrderMeterTriggerPatchDTO;
import com.grash.model.WorkOrderMeterTrigger;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkOrderMeterTriggerMapper {
    WorkOrderMeterTrigger updateWorkOrderMeterTrigger(@MappingTarget WorkOrderMeterTrigger entity, WorkOrderMeterTriggerPatchDTO dto);
}
