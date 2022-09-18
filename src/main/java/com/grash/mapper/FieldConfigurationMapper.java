package com.grash.mapper;

import com.grash.dto.FieldConfigurationPatchDTO;
import com.grash.model.FieldConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FieldConfigurationMapper {
    FieldConfiguration updateFieldConfiguration(@MappingTarget FieldConfiguration entity, FieldConfigurationPatchDTO dto);
}
