package com.grash.mapper;

import com.grash.dto.LaborCostPatchDTO;
import com.grash.model.LaborCost;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface LaborCostMapper {
    LaborCost updateLaborCost(@MappingTarget LaborCost entity, LaborCostPatchDTO dto);

    @Mappings({})
    LaborCostPatchDTO toDto(LaborCost model);
}
