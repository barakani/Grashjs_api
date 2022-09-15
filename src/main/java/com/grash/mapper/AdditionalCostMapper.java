package com.grash.mapper;

import com.grash.dto.AdditionalCostPatchDTO;
import com.grash.model.AdditionalCost;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdditionalCostMapper {
    AdditionalCost updateAdditionalCost(@MappingTarget AdditionalCost entity, AdditionalCostPatchDTO dto);
}
