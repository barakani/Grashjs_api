package com.grash.mapper;

import com.grash.dto.DeprecationPatchDTO;
import com.grash.model.Deprecation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DeprecationMapper {
    Deprecation updateDeprecation(@MappingTarget Deprecation entity, DeprecationPatchDTO dto);
}
