package com.grash.mapper;

import com.grash.dto.LaborPatchDTO;
import com.grash.model.Labor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LaborMapper {
    Labor updateLabor(@MappingTarget Labor entity, LaborPatchDTO dto);
}
