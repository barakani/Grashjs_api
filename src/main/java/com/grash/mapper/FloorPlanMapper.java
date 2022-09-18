package com.grash.mapper;

import com.grash.dto.FloorPlanPatchDTO;
import com.grash.model.FloorPlan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FloorPlanMapper {
    FloorPlan updateFloorPlan(@MappingTarget FloorPlan entity, FloorPlanPatchDTO dto);
}
